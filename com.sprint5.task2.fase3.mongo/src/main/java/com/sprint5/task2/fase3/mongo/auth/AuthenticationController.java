package com.sprint5.task2.fase3.mongo.auth;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Authentication", description = "This controller allows to register, update or authenticate the user and generates the access token to play the game")
@SecurityRequirement(name = "jwtopenapi")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

        private final AuthenticationService service;

        @PostMapping("/register")
        public ResponseEntity<?> register(@RequestBody RegisterRequest register) {
            try {
                return ResponseEntity.ok(service.register(register));
            } catch (ResponseStatusException e) {
                Map<String, Object> error = new HashMap<>();
                error.put("Message", e.getMessage());
                error.put("Reason", e.getReason());
                return new ResponseEntity<Map<String, Object>>(error, HttpStatus.BAD_REQUEST);
            }
        }

        @PostMapping("/authenticate")
        public ResponseEntity<?> authenticate (@RequestBody AuthenticationRequest request){
            try {
                return ResponseEntity.ok(service.authenticate(request));
            }
                catch (ResponseStatusException e) {
                Map<String, Object> error = new HashMap<>();
                error.put("Message", e.getMessage());
                error.put("Reason", e.getReason());
                return new ResponseEntity<Map<String, Object>>(error, HttpStatus.BAD_REQUEST);
                }
            }
}
