package dev.apma.cnat.userservice.controller;


import dev.apma.cnat.userservice.error.UserAlreadyExistException;
import dev.apma.cnat.userservice.model.User;
import dev.apma.cnat.userservice.model.UserRepository;
import dev.apma.cnat.userservice.response.GenericResponse;
import dev.apma.cnat.userservice.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class MainController {
    private final static Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public GenericResponse registerUser(@Valid @RequestBody User req) {
        LOGGER.info("/register {}", req);
        req.setPassword(passwordEncoder.encode(req.getPassword()));
        /// TODO: Verify email to enable account
        req.setEnabled(true);
        try {
            userService.registerNewUser(req);
            return new GenericResponse("User registered");
        } catch (UserAlreadyExistException e) {
            LOGGER.error("An account for that email already exists");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An account for that email already exists");
        }
    }

    @PostMapping("/auth")
    public GenericResponse authenticate(@RequestBody User req) {
        LOGGER.info("/auth {}", req);
        User user = userRepo.findByEmail(req.getEmail());
        if (user != null && passwordEncoder.matches(req.getPassword(), user.getPassword()) && user.isEnabled()) {
            return new GenericResponse("OK");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication fail");
    }
}
