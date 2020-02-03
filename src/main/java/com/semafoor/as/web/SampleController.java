package com.semafoor.as.web;

import org.springframework.http.ResponseEntity;
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
}
