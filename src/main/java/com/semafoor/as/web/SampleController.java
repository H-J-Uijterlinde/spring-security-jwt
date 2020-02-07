package com.semafoor.as.web;

import com.semafoor.as.config.SecurityConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple Controller for testing the security implementation. Only users who have successfully authenticated should be
 * able to see the messages defined in the methods, see {@link SecurityConfig}
 */

@RequestMapping("/protected")
@RestController
public class SampleController {

    @GetMapping("/1")
    public ResponseEntity<String> getExampleMessage() {
        return ResponseEntity.ok().body("An Example Message, needs authentication");
    }

    // This endpoint should only be accessible for users with "ROLE_ADMIN".
    @Secured("ROLE_ADMIN")
    @GetMapping("/2")
    public ResponseEntity<String> getExampleAdminMessage() {
        return ResponseEntity.ok().body("This message should only be seen by admins");
    }

    @GetMapping("/3")
    public ResponseEntity<String> getAutoReplaceTestMessage() {
        return ResponseEntity.ok().body("If we can see this message, that means the warfile in the tomcat webapps folder" +
                " gets replaced automatically. No delete necessary. So hard..");
    }

    @GetMapping("/4")
    public ResponseEntity<String> getFirstBambooUpdate() {
        return ResponseEntity.ok().body("This is an example message created to test our CI pipeline");
    }
}
