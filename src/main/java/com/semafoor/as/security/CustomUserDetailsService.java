package com.semafoor.as.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private Map<String, User> users = new HashMap<>();
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public CustomUserDetailsService() {
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User admin = new User("admin", bCryptPasswordEncoder.encode("admin"), true, true, true, true);
        admin.addAuthorities("ROLE_USER", "ROLE_ADMIN");
        users.put(admin.getUsername(), admin);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Trying to find user with username: {}", username);

        User user = this.users.get(username);

        if (user == null) {
            log.info("About to throw a UsernameNotFoundException");
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }
}
