package com.esgi.securivault.auth;


import com.google.firebase.auth.FirebaseAuthException;
import com.esgi.securivault.auth.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthResource {

    private final AuthService authService;

    public AuthResource(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public FirebaseLogInResponse login(FirebaseLogInRequest firebaseLogInRequest) {
        return authService.login(firebaseLogInRequest.email(), firebaseLogInRequest.password());
    }

    @GetMapping("/refresh-token")
    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        return authService.exchangeRefreshToken(refreshTokenRequest.refreshToken());
    }

    @PostMapping
    public void create(@Valid @RequestBody FirebaseSignInRequest firebaseSignInRequest) throws FirebaseAuthException {
        authService.create(firebaseSignInRequest);
    }

}