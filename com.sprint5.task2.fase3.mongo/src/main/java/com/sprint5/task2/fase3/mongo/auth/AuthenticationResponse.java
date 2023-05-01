package com.sprint5.task2.fase3.mongo.auth;
import lombok.*;

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
}
