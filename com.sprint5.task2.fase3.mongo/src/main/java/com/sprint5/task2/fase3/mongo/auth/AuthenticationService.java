package com.sprint5.task2.fase3.mongo.auth;
import com.sprint5.task2.fase3.mongo.config.JwtService;
import com.sprint5.task2.fase3.mongo.entity.Game;
import com.sprint5.task2.fase3.mongo.entity.User;
import com.sprint5.task2.fase3.mongo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {
        String IdForDb;
        if(!request.getEmail().equals("")) {
            Optional<User> userDb = repository.findByEmail(request.getEmail());
            if (userDb.isPresent()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Te user with email " + request.getEmail() + " exists.");
            }
        }
        List<User> lista = repository.findAll();
        OptionalInt ultimoId = lista.stream().mapToInt(x -> Integer.valueOf(x.getId())).max();
        if(ultimoId.isPresent()){
            int ultimoIdInt = ultimoId.getAsInt();
            int nuevoId = ultimoIdInt + 1;
            IdForDb = Integer.toString(nuevoId);
            }else {
            IdForDb = "1";
        }
        List<Game> gameList = new ArrayList<>();
        User user = new User();
        user.setId(IdForDb);
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreated(LocalDateTime.now());
        user.setGames(gameList);
        //  TODO : Role

        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * This method updates the name of the user
     * @param  // RegisterRequest request
     * @return AuthenticationResponse
     */

    public AuthenticationResponse update (RegisterRequest register){
        log.info("Register request: " + register);
        Optional<User> userDb = repository.findByEmail(register.getEmail());
        if (!userDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Te player with email " + register.getEmail() + " does not exists.");
        }
        User userUpdate = userDb.get();
        userUpdate.setName(register.getName());
        User updated = repository.save(userUpdate);
        var jwtToken = jwtService.generateToken(updated);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                ));
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow ();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
