package dev.apma.cnat.userservice.service;


import dev.apma.cnat.userservice.dto.UserDTO;
import dev.apma.cnat.userservice.exception.UserAlreadyExistsException;
import dev.apma.cnat.userservice.exception.UserAuthenticationFailException;
import dev.apma.cnat.userservice.exception.UserDoesNotExistException;
import dev.apma.cnat.userservice.model.User;
import dev.apma.cnat.userservice.repository.UserRepository;
import dev.apma.cnat.userservice.request.UserAuthRequest;
import dev.apma.cnat.userservice.request.UserDeleteRequest;
import dev.apma.cnat.userservice.request.UserRegisterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * An implementation of the {@code UserService} interface.
 *
 * @author Amir Parsa Mahdian
 * @see dev.apma.cnat.userservice.service.UserService
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * The user repository
     */
    private final UserRepository userRepo;

    /**
     * The {@code PasswordEncoder} used to encrypt and validate passwords
     */
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(UserRegisterRequest req) throws UserAlreadyExistsException {
        if (emailExists(req.email())) {
            throw new UserAlreadyExistsException("An account with this email address already exists: " + req.email());
        }
        userRepo.save(new User(req.email(),
                passwordEncoder.encode(req.password()),
                req.firstName(),
                req.lastName(),
                true));
    }

    @Override
    public UserDTO find(String email) throws UserDoesNotExistException {
        var u = userRepo.findByEmail(email);
        if (u.isEmpty()) {
            throw new UserDoesNotExistException();
        }
        return new UserDTO(u.get().getEmail(), u.get().getFirstName(), u.get().getLastName());
    }

    @Override
    public void authenticate(UserAuthRequest req) throws UserDoesNotExistException, UserAuthenticationFailException {
        var user = userRepo.findByEmail(req.email());
        if (user.isEmpty()) {
            throw new UserDoesNotExistException();
        } else if (req.password() == null) {
            throw new UserAuthenticationFailException();
        } else if (!(passwordEncoder.matches(req.password(), user.get().getPassword()) && user.get().isEnabled())) {
            throw new UserAuthenticationFailException();
        }
    }

    @Override
    public void delete(UserDeleteRequest req) throws UserAuthenticationFailException {
        try {
            authenticate(new UserAuthRequest(req.email(), req.password()));
            userRepo.deleteByEmail(req.email());
        } catch (UserDoesNotExistException e) {
            LOGGER.info("Requested to delete non existing user [{}]", req.email());
        }
    }

    private boolean emailExists(String email) {
        return userRepo.findByEmail(email).isPresent();
    }
}
