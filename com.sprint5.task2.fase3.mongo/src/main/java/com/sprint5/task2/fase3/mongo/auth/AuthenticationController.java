package com.sprint5.task2.fase3.mongo.auth;

import com.sprint5.task2.fase3.mongo.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Authentication", description = "This controller allows to register, update or authenticate the user and generates the access token to play the game")
@SecurityRequirement(name = "jwtopenapi")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private static Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService service;


    /*
     * This method creates a User
     *
     * @param RegisterRequest register
     * @return ResponseEntity<AuthenticationResponse>
     */
    @Operation(summary = "Adds a new User", description = "Creates a new User and saves it in the database")
    @ApiResponse(responseCode = "200", description = "User created correctly", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = RegisterRequest.class))})
    @ApiResponse(responseCode = "400", description = "The User already exists", content = @Content)
    @PostMapping("/register")
        public ResponseEntity<?> register(@RequestBody RegisterRequest register) {
            try {
                return ResponseEntity.ok(service.register(register));
            }catch (ResponseStatusException e){
                return new ResponseEntity<Map<String,Object>>(this.message(e), e.getStatusCode());
            }
        }

    /*
     * PUT /User: Updates the name of an existing player.
     */
    @Operation(summary= "Update User", description = "Updates the name of an existing user")
    @ApiResponse(responseCode = "201", description = "User updated correctly", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = RegisterRequest.class))})
    @ApiResponse(responseCode = "403", description = "Not authenticated", content = @Content)
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody RegisterRequest register) {

        log.info("Register request: " + register);
        try {

            service.update(register);
            return ResponseEntity.ok(register);
        }catch (ResponseStatusException e){
            return new ResponseEntity<Map<String,Object>>(this.message(e), e.getStatusCode());
        }
    }

    /**
     * This method creates a User
     *
     * @param request
     * @return ResponseEntity<AuthenticationResponse>
     */
    @Operation(summary = "Authenticates User", description = "Authenticas the user and returns the token")
    //@ApiResponse(responseCode = "200", description = "User authenticated", content = @Content)
    //@ApiResponse(responseCode = "403", description = "Not authenticated", content = @Content)
        @PostMapping("/authenticate")
        public ResponseEntity<AuthenticationResponse> authenticate (@RequestBody AuthenticationRequest request){
                return ResponseEntity.ok(service.authenticate(request));
        }

    /*
     * This method handles the exception and returns message and reason
     *
     */

    private Map<String, Object> message(ResponseStatusException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("Message", e.getMessage());
        error.put("Reason", e.getReason());
        return error;
    }

}
