package com.semafoor.as.security;

import com.semafoor.as.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Custom implementation of the {@link UserDetailsService} interface. By implementing the loadByUsername method the
 * logic for how to obtain userDetails can be provided.
 */

@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * This method defines how to obtain {@link UserDetails}. In this case the {@link UserRepository} is called to find
     * a user by username. This method is not allowed to return null! If no user is found for a given username, an
     * exception must be thrown.
     *
     * @param username username for authentication request.
     *
     * @return {@link UserDetails}
     *
     * @throws UsernameNotFoundException throw exception if user not found.
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Trying to find user with username: {}", username);

        return userRepository.findByUsername(username).orElseThrow(() -> {
            log.debug("About to throw a UsernameNotFoundException");
            throw new UsernameNotFoundException("User not found");
        });
    }
}
