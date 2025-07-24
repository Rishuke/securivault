package com.esgi.securivault.auth;

import com.google.firebase.auth.FirebaseAuthException;
import com.esgi.securivault.auth.dto.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthResource {

    private final AuthService authService;

    public AuthResource(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public FirebaseLogInResponse loginWithEmailPassword(@Valid @RequestBody FirebaseLogInRequest firebaseLogInRequest) {
        return authService.loginWithEmailPassword(firebaseLogInRequest.email(), firebaseLogInRequest.password());
    }

    @PostMapping("/google")
    public FirebaseLogInResponse loginWithGoogle(@RequestBody Map<String, String> payload) {
        String accessToken = payload.get("access_token");
        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalArgumentException("access_token is required");
        }
        return authService.loginWithGoogle(accessToken);
    }

    @GetMapping("/refresh-token")
    public RefreshTokenResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.exchangeRefreshToken(refreshTokenRequest.refreshToken());
    }

    @PostMapping("/register")
    public void create(@Valid @RequestBody FirebaseSignInRequest firebaseSignInRequest) throws FirebaseAuthException {
        authService.create(firebaseSignInRequest);
    }

}