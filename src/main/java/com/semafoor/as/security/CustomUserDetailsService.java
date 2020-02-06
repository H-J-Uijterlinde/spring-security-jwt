package com.semafoor.as.security;

import com.semafoor.as.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Trying to find user with username: {}", username);

        return userRepository.findByUsername(username).orElseThrow(() -> {
            log.info("About to throw a UsernameNotFoundException");
            throw new UsernameNotFoundException("User not found");
        });
    }
}
