package dev.apma.cnat.userservice.controller;


import dev.apma.cnat.userservice.error.UserAlreadyExistException;
import dev.apma.cnat.userservice.requests.UserAuthRequest;
import dev.apma.cnat.userservice.requests.UserDeleteRequest;
import dev.apma.cnat.userservice.requests.UserRegisterRequest;
import dev.apma.cnat.userservice.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class MainController {
    private final static Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    private final UserService userSvc;

    @Autowired
    public MainController(UserService userSvc) {
        this.userSvc = userSvc;
    }

    @PostMapping("")
    public void register(@Valid @RequestBody UserRegisterRequest req) {
        LOGGER.info("post / {}", req);
        try {
            userSvc.register(req);
        } catch (UserAlreadyExistException e) {
            LOGGER.error("An account for that email already exists");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An account for that email already exists");
        }
    }

    @PostMapping("/auth")
    public void authenticate(@Valid @RequestBody UserAuthRequest req) {
        LOGGER.info("post /auth {}", req);
        if (!userSvc.authenticate(req)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication fail");
        }
    }

    @DeleteMapping("")
    public void delete(@Valid @RequestBody UserDeleteRequest req) {
        LOGGER.info("delete / {}", req);
        if (!userSvc.authenticate(new UserAuthRequest(req.email(), req.password()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication fail");
        }
        userSvc.delete(req);
    }
}
