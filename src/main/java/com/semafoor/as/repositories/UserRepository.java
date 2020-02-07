package com.semafoor.as.repositories;

import com.semafoor.as.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Repository for crud operations on {@link User} objects.
 */

public interface UserRepository extends CrudRepository<User, Long> {

    // required by userDetailsService
    Optional<User> findByUsername(String username);
}
