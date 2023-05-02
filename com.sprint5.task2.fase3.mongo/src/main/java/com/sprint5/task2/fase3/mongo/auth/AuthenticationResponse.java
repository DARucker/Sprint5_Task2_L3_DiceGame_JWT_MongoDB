package com.sprint5.task2.fase3.mongo.auth;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    @Schema(description = "This field will return the token generated by the application.", example = "eyJhbGciOiJIUzI1Ni", required = false)
    private String token;
}
