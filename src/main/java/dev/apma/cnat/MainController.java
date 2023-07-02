package dev.apma.cnat;


import dev.apma.cnat.service.UserService;
import dev.apma.cnat.web.error.UserAlreadyExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    private final static Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(path = "/register")
    @ResponseBody
    public ResponseEntity<String> createUser(@RequestBody User req) {
        LOGGER.info("/register %s".formatted(req));
        req.setPassword(passwordEncoder.encode(req.getPassword()));
        /// TODO: Verify email to enable account
        req.setEnabled(true);
        try {
            userService.registerNewUser(req);
            return new ResponseEntity<>("User registered.", HttpStatus.OK);
        } catch (UserAlreadyExistException uaeEx) {
            LOGGER.error("An account for that username/email already exists.");
            return new ResponseEntity<>("An account for that username/email already exists.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/authenticate")
    @ResponseBody
    public boolean authenticate(@RequestBody User req) {
        LOGGER.info("/authenticate %s".formatted(req));
        User user = userRepo.findByEmail(req.getEmail());
        return user != null && passwordEncoder.matches(req.getPassword(), user.getPassword()) && user.isEnabled();
    }
}
