package com.semafoor.as.web;

import com.semafoor.as.config.SecurityConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple controller for testing security implementation. The endpoints defined here should not be secured, see
 * {@link SecurityConfig}
 */

@RequestMapping("/open")
@RestController
public class openController {

    @GetMapping("/1")
    public ResponseEntity<String> getUnsecuredMessage() {
        return ResponseEntity.ok().body("This message is for everyone to read");
    }
}
