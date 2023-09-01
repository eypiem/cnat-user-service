package dev.apma.cnat.userservice.repository;


import dev.apma.cnat.userservice.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    void deleteByEmail(String email);
}
