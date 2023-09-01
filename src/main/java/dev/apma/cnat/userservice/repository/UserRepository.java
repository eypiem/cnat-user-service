package dev.apma.cnat.userservice.repository;


import dev.apma.cnat.userservice.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * This interface extends the {@code CrudRepository} interface and allows Spring Boot Data JPA to perform operations
 * of the <i>users</i> database.
 *
 * @author Amir Parsa Mahdian
 */
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    void deleteByEmail(String email);
}
