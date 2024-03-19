//referred https://www.youtube.com/watch?v=lA18U8dGKF8&t=1033s
package com.uol.birding.auth;

import com.uol.birding.entity.User;
import com.uol.birding.enums.UserType;
import com.uol.birding.repository.UserRepository;
import com.uol.birding.util.BirdingMessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    public ResponseEntity register(RegisterRequest req) {
        try {
            User db_user = userRepository.findByEmail(req.getEmail()).orElse(null);
            if (db_user != null) throw new Exception("User Already Exists");

            var user = User.builder()
                    .firstName(req.getFirstName())
                    .lastName(req.getLastName())
                    .email(req.getEmail())
                    .password(encoder.encode(req.getPassword()))
                    .userType(UserType.USER)
                    .build();
            userRepository.save(user);
            var token = authService.generateToken(user);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(AuthenticationResponse.builder()
                            .token(token)
                            .userType(user.getUserType())
                            .name(user.getFirstName() + " " + user.getLastName())
                            .build());
        }catch (Exception e){
            return BirdingMessageUtils.createErrorResponse(e.getMessage());
        }
    }

    public ResponseEntity registerMod(RegisterRequest req, String userName) {
        try {
            User admin = userRepository.findByEmail(userName).orElse(null);
            if (admin == null || UserType.ADMIN!=admin.getUserType()) throw new Exception("Illegal request.");
            User db_user = userRepository.findByEmail(req.getEmail()).orElse(null);
            if (db_user != null && UserType.USER!=db_user.getUserType()) throw new Exception("User already has privilege.");
            User user = User.builder()
                    .firstName(req.getFirstName())
                    .lastName(req.getLastName())
                    .email(req.getEmail())
                    .password(encoder.encode(req.getPassword()))
                    .userType(UserType.MODERATOR)
                    .build();
            if(db_user != null) user.setId(db_user.getId());
            userRepository.save(user);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"success\":\"Moderator has been created.\"}");
        } catch (Exception e) {
            return BirdingMessageUtils.createErrorResponse(e.getMessage());
        }
    }

    public ResponseEntity authenticate(AuthenticationRequest req) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
            var user = userRepository.findByEmail(req.getEmail()).orElseThrow(() -> new Exception("User not found!"));
            var token = authService.generateToken(user);
            return ResponseEntity.ok(
                    AuthenticationResponse
                            .builder()
                            .token(token)
                            .userType(user.getUserType())
                            .name(user.getFirstName() + " " + user.getLastName())
                            .build());
        } catch (Exception e) {
            return BirdingMessageUtils.createErrorResponse(e.getMessage());
        }
    }
}
