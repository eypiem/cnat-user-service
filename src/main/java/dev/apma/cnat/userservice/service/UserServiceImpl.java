package dev.apma.cnat.userservice.service;


import dev.apma.cnat.userservice.exception.UserAlreadyExistsException;
import dev.apma.cnat.userservice.exception.UserAuthenticationFailException;
import dev.apma.cnat.userservice.model.User;
import dev.apma.cnat.userservice.model.UserRepository;
import dev.apma.cnat.userservice.request.UserAuthRequest;
import dev.apma.cnat.userservice.request.UserDeleteRequest;
import dev.apma.cnat.userservice.request.UserRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

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
    public void authenticate(UserAuthRequest req) throws UserAuthenticationFailException {
        var user = userRepo.findByEmail(req.email());
        if (!(user.isPresent() && passwordEncoder.matches(req.password(), user.get().getPassword()) && user.get()
                .isEnabled())) {
            throw new UserAuthenticationFailException();
        }
    }

    @Override
    public void delete(UserDeleteRequest req) {
        userRepo.deleteByEmail(req.email());
    }

    private boolean emailExists(String email) {
        return userRepo.findByEmail(email).isPresent();
    }
}
