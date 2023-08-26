package dev.apma.cnat.userservice.controller;


import dev.apma.cnat.userservice.exception.UserAlreadyExistsException;
import dev.apma.cnat.userservice.exception.UserAuthenticationFailException;
import dev.apma.cnat.userservice.request.UserAuthRequest;
import dev.apma.cnat.userservice.request.UserDeleteRequest;
import dev.apma.cnat.userservice.request.UserRegisterRequest;
import dev.apma.cnat.userservice.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    private final static Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    private final UserService userSvc;

    @Autowired
    public MainController(UserService userSvc) {
        this.userSvc = userSvc;
    }

    @PostMapping("")
    public void register(@Valid @RequestBody UserRegisterRequest req) throws UserAlreadyExistsException {
        LOGGER.info("post / {}", req);
        userSvc.register(req);
    }

    @PostMapping("/auth")
    public void authenticate(@Valid @RequestBody UserAuthRequest req) throws UserAuthenticationFailException {
        LOGGER.info("post /auth {}", req);
        userSvc.authenticate(req);
    }

    @DeleteMapping("")
    public void delete(@Valid @RequestBody UserDeleteRequest req) throws UserAuthenticationFailException {
        LOGGER.info("delete / {}", req);
        userSvc.authenticate(new UserAuthRequest(req.email(), req.password()));
        userSvc.delete(req);
    }
}
