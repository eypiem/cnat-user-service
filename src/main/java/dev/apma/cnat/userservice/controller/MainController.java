package dev.apma.cnat.userservice.controller;


import dev.apma.cnat.userservice.dto.UserDTO;
import dev.apma.cnat.userservice.exception.UserAlreadyExistsException;
import dev.apma.cnat.userservice.exception.UserAuthenticationFailException;
import dev.apma.cnat.userservice.exception.UserDoesNotExistException;
import dev.apma.cnat.userservice.request.UserAuthRequest;
import dev.apma.cnat.userservice.request.UserDeleteRequest;
import dev.apma.cnat.userservice.request.UserRegisterRequest;
import dev.apma.cnat.userservice.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MainController {
    private final static Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    private final UserService userSvc;

    @Autowired
    public MainController(UserService userSvc) {
        this.userSvc = userSvc;
    }

    @PostMapping("")
    public void register(@RequestBody @Valid UserRegisterRequest req) throws UserAlreadyExistsException {
        LOGGER.info("post  {}", req);
        userSvc.register(req);
    }

    @GetMapping("")
    public UserDTO get(@RequestParam String email) throws UserDoesNotExistException {
        LOGGER.info("get ?email={}", email);
        return userSvc.find(email);
    }

    @PostMapping("/auth")
    public void authenticate(@RequestBody @Valid UserAuthRequest req) throws UserAuthenticationFailException {
        LOGGER.info("post /auth {}", req);
        try {
            userSvc.authenticate(req);
        } catch (UserDoesNotExistException e) {
            /// NOTE: Changing exception to reduce the information about user accounts.
            throw new UserAuthenticationFailException();
        }
    }

    @DeleteMapping("")
    public void delete(@RequestBody @Valid UserDeleteRequest req) throws UserAuthenticationFailException {
        LOGGER.info("delete {}", req);
        userSvc.delete(req);
    }
}
