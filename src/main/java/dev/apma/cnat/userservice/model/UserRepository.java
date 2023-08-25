package dev.apma.cnat.userservice.model;


import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    void deleteByEmail(String email);
}
