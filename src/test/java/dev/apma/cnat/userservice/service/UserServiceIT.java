package dev.apma.cnat.userservice.service;


import dev.apma.cnat.userservice.exception.UserAlreadyExistsException;
import dev.apma.cnat.userservice.exception.UserAuthenticationFailException;
import dev.apma.cnat.userservice.exception.UserDoesNotExistException;
import dev.apma.cnat.userservice.request.UserAuthRequest;
import dev.apma.cnat.userservice.request.UserDeleteRequest;
import dev.apma.cnat.userservice.request.UserRegisterRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest").withDatabaseName("test")
            .withUsername("username")
            .withPassword("password");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserService userSvc;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Register {

        @Test
        void non_existent_user() {
            var req = new UserRegisterRequest(UUID.randomUUID() + "@test.com", "password", "fn", "ln");
            assertDoesNotThrow(() -> userSvc.register(req));
        }

        @Test
        void existent_user() throws UserAlreadyExistsException {
            var req = new UserRegisterRequest(UUID.randomUUID() + "@test.com", "password", "fn", "ln");
            userSvc.register(req);

            assertThrows(UserAlreadyExistsException.class, () -> userSvc.register(req));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Find {
        public static final String EMAIL = UUID.randomUUID() + "@test.com";
        public static final String PASSWORD = "password";
        public static final String F_NAME = "fn";
        public static final String L_NAME = "ln";

        @BeforeAll
        void setup() throws UserAlreadyExistsException {
            userSvc.register(new UserRegisterRequest(EMAIL, PASSWORD, F_NAME, L_NAME));
        }

        @AfterAll
        void tearDown() throws UserAuthenticationFailException {
            userSvc.delete(new UserDeleteRequest(EMAIL, PASSWORD));
        }

        @Test
        void existent_user() throws UserDoesNotExistException {
            var u = userSvc.find(EMAIL);

            assertAll(() -> assertEquals(EMAIL, u.email()),
                    () -> assertEquals(F_NAME, u.firstName()),
                    () -> assertEquals(L_NAME, u.lastName()));
        }

        @Test
        void non_existent_user() {
            assertThrows(UserDoesNotExistException.class, () -> userSvc.find(UUID.randomUUID() + "@test.com"));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Authenticate {
        public static final String EMAIL = UUID.randomUUID() + "@test.com";
        public static final String PASSWORD = "password";

        @BeforeAll
        void setup() throws UserAlreadyExistsException {
            userSvc.register(new UserRegisterRequest(EMAIL, PASSWORD, "fn", "ln"));
        }

        @AfterAll
        void tearDown() throws UserAuthenticationFailException {
            userSvc.delete(new UserDeleteRequest(EMAIL, PASSWORD));
        }

        @Test
        void existent_user() {
            assertDoesNotThrow(() -> userSvc.authenticate(new UserAuthRequest(EMAIL, PASSWORD)));
        }

        @Test
        void invalid_email() {
            assertThrows(UserDoesNotExistException.class,
                    () -> userSvc.authenticate(new UserAuthRequest(UUID.randomUUID() + "@test.com", PASSWORD)));
        }

        @Test
        void null_email() {
            assertThrows(UserDoesNotExistException.class,
                    () -> userSvc.authenticate(new UserAuthRequest(null, PASSWORD)));
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "Password"})
        void invalid_password(String invalidPassword) {
            assertThrows(UserAuthenticationFailException.class,
                    () -> userSvc.authenticate(new UserAuthRequest(EMAIL, invalidPassword)));
        }

        @Test
        void null_password() {
            assertThrows(UserAuthenticationFailException.class,
                    () -> userSvc.authenticate(new UserAuthRequest(EMAIL, null)));
        }

        @Test
        void non_existent_user() {
            assertThrows(UserDoesNotExistException.class,
                    () -> userSvc.authenticate(new UserAuthRequest(UUID.randomUUID() + "@test.com", PASSWORD)));
        }
    }

    @Nested
    class Delete {
        public static final String EMAIL = UUID.randomUUID() + "@test.com";
        public static final String PASSWORD = "password";

        @BeforeEach
        void setup() throws UserAlreadyExistsException {
            userSvc.register(new UserRegisterRequest(EMAIL, PASSWORD, "fn", "ln"));
        }

        @AfterEach
        void tearDown() throws UserAuthenticationFailException {
            userSvc.delete(new UserDeleteRequest(EMAIL, PASSWORD));
        }

        @Test
        void existent_user() {
            assertAll(() -> assertDoesNotThrow(() -> userSvc.delete(new UserDeleteRequest(EMAIL, PASSWORD))),
                    () -> assertThrows(UserDoesNotExistException.class, () -> userSvc.find(EMAIL)));
        }

        @Test
        void invalid_email() {
            assertDoesNotThrow(() -> userSvc.delete(new UserDeleteRequest(UUID.randomUUID() + "@test.com", PASSWORD)));
        }

        @Test
        void null_email() {
            assertDoesNotThrow(() -> userSvc.delete(new UserDeleteRequest(null, PASSWORD)));
        }


        @ParameterizedTest
        @ValueSource(strings = {"", " ", "Password"})
        void invalid_password(String invalidPassword) {
            assertThrows(UserAuthenticationFailException.class,
                    () -> userSvc.delete(new UserDeleteRequest(EMAIL, invalidPassword)));
        }

        @Test
        void null_password() {
            assertThrows(UserAuthenticationFailException.class,
                    () -> userSvc.delete(new UserDeleteRequest(EMAIL, null)));
        }

        @Test
        void non_existent_user() {
            assertDoesNotThrow(() -> userSvc.delete(new UserDeleteRequest(UUID.randomUUID() + "@test.com", PASSWORD)));
        }
    }
}
