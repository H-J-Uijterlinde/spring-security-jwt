package com.semafoor.as.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/examples")
@RestController
public class SampleController {

    @GetMapping("/1")
    public ResponseEntity<String> getExampleMessage() {
        return ResponseEntity.ok().body("An Example Message");
    }

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
