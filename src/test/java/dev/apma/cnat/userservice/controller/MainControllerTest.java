package dev.apma.cnat.userservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import dev.apma.cnat.userservice.dto.UserDTO;
import dev.apma.cnat.userservice.request.UserAuthRequest;
import dev.apma.cnat.userservice.request.UserDeleteRequest;
import dev.apma.cnat.userservice.request.UserRegisterRequest;
import dev.apma.cnat.userservice.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private static UserService userSvc;

    @Nested
    class Register {

        @BeforeAll
        static void setup() throws Exception {
            doNothing().when(userSvc).register(isA(UserRegisterRequest.class));
        }

        @Test
        void valid() throws Exception {
            var urr = new UserRegisterRequest("1@test.com", "password1", "fn1", "ln1");

            mockMvc.perform(post("").contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(urr))).andExpect(status().isOk());
        }

        @Test
        void invalid_empty_body() throws Exception {
            mockMvc.perform(post("").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "a", ".a@a.com", "a.com", "a@a"})
        void invalid_email(String email) throws Exception {
            var urr = new UserRegisterRequest(email, "password1", "fn1", "ln1");

            mockMvc.perform(post("").contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(urr))).andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "1234567"})
        void invalid_password(String password) throws Exception {
            var urr = new UserRegisterRequest("a@a.com", password, "fn1", "ln1");

            mockMvc.perform(post("").contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(urr))).andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "a"})
        void invalid_firstName(String firstName) throws Exception {
            var urr = new UserRegisterRequest("a@a.com", "password1", firstName, "ln1");

            mockMvc.perform(post("").contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(urr))).andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "a"})
        void invalid_lastName(String lastName) throws Exception {
            var urr = new UserRegisterRequest("a@a.com", "password1", "fn1", lastName);

            mockMvc.perform(post("").contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(urr))).andExpect(status().isBadRequest());
        }
    }

    @Nested
    class Get {

        @BeforeAll
        static void setup() throws Exception {
            when(userSvc.find(isA(String.class))).thenReturn(new UserDTO("test1@test.com", "fn", "ln"));
        }

        @Test
        void valid() throws Exception {
            mockMvc.perform(get("").param("email", "test1@test.com")).andExpect(status().isOk());
        }

        @Test
        void invalid_empty_param() throws Exception {
            mockMvc.perform(get("")).andExpect(status().isBadRequest());
        }
    }

    @Nested
    class Auth {

        @BeforeAll
        static void setup() throws Exception {
            doNothing().when(userSvc).authenticate(isA(UserAuthRequest.class));
        }

        @Test
        void valid() throws Exception {
            var req = new UserAuthRequest("1@test.com", "password1");

            mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(req))).andExpect(status().isOk());
        }

        @Test
        void invalid_empty_body() throws Exception {
            mockMvc.perform(post("/auth")).andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "a", ".a@a.com", "a.com", "a@a"})
        void invalid_email(String email) throws Exception {
            var req = new UserAuthRequest(email, "password1");

            mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(req))).andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "1234567"})
        void invalid_password(String password) throws Exception {
            var req = new UserAuthRequest("1@test.com", password);

            mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(req))).andExpect(status().isBadRequest());
        }
    }

    @Nested
    class Delete {

        @BeforeAll
        static void setup() throws Exception {
            doNothing().when(userSvc).delete(isA(UserDeleteRequest.class));
        }

        @Test
        void valid() throws Exception {
            var udr = new UserDeleteRequest("1@test.com", "password1");

            mockMvc.perform(delete("").contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(udr))).andExpect(status().isOk());
        }

        @Test
        void empty_body() throws Exception {
            mockMvc.perform(delete("")).andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "a", ".a@a.com", "a.com", "a@a"})
        void invalid_email(String email) throws Exception {
            var udr = new UserDeleteRequest(email, "password1");

            mockMvc.perform(delete("").contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(udr))).andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "1234567"})
        void invalid_password(String password) throws Exception {
            var udr = new UserDeleteRequest("1@test.com", password);

            mockMvc.perform(delete("").contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(udr))).andExpect(status().isBadRequest());
        }
    }
}
