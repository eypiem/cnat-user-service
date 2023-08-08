package dev.apma.cnat.userservice.service;


import dev.apma.cnat.userservice.error.UserAlreadyExistException;
import dev.apma.cnat.userservice.model.User;
import dev.apma.cnat.userservice.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public User registerNewUser(User user) throws UserAlreadyExistException {
        if (emailExists(user.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: " + user.getEmail());
        }
        return userRepo.save(user);
    }

    private boolean emailExists(String email) {
        return userRepo.findByEmail(email) != null;
    }
}
