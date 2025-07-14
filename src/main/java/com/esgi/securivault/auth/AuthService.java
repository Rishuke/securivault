package com.esgi.securivault.auth;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.esgi.securivault.auth.dto.*;
import com.esgi.securivault.errors.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
public class AuthService {

    private final FirebaseAuth firebaseAuth;
    private static final String API_KEY_PARAM = "key";
    private static final String INVALID_CREDENTIALS_ERROR = "INVALID_LOGIN_CREDENTIALS";
    private static final String SIGN_IN_BASE_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword";
    private static final String REFRESH_TOKEN_GRANT_TYPE = "refresh_token";
    private static final String INVALID_REFRESH_TOKEN_ERROR = "INVALID_REFRESH_TOKEN";
    private static final String REFRESH_TOKEN_BASE_URL = "https://securetoken.googleapis.com/v1/token";
    private static final String DUPLICATE_ACCOUNT_ERROR = "EMAIL_EXISTS";

    @Value("${securivault.firebase.web-api-key}")
    private String webApiKey;

    public AuthService(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public void create(FirebaseSignInRequest firebaseSignInRequest) throws FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest();
        request.setEmail(firebaseSignInRequest.email());
        request.setPassword(firebaseSignInRequest.password());
        request.setEmailVerified(Boolean.TRUE);

        try {
            firebaseAuth.createUser(request);
            

        } catch (FirebaseAuthException exception) {
            if (exception.getMessage().contains(DUPLICATE_ACCOUNT_ERROR)) {
                throw new IllegalArgumentException("User already exists");
            }
            throw exception;
        }

    }
    public FirebaseToken verifyToken(String token) {
        /*try {
            return firebaseAuth.verifyIdToken(token);
        } catch (FirebaseAuthException e) {
            throw new UnauthorizedException("Invalid token");
        }*/
        return null;
    }

    public FirebaseLogInResponse login(String email, String password) {
    FirebaseLogInResponse response = sendLogInRequest(new FirebaseLogInRequest(email, password));
    System.out.println(response);
    /*try {
        firebaseAuth.verifyIdToken(response.idToken());
    } catch (FirebaseAuthException e) {
        throw new UnauthorizedException("Generated token is invalid");

    }*/
    
    return response;

}

    private FirebaseLogInResponse sendLogInRequest(FirebaseLogInRequest firebaseLogInRequest) {
        
        try {
            return RestClient.create(SIGN_IN_BASE_URL)
                    .post()
                    .uri(uriBuilder -> uriBuilder.queryParam(API_KEY_PARAM, webApiKey).build())
                    .body(firebaseLogInRequest)
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(FirebaseLogInResponse.class);
        } catch (HttpClientErrorException exception) {
            if (exception.getResponseBodyAsString().contains(INVALID_CREDENTIALS_ERROR)) {
                throw new UnauthorizedException("Invalid credentials");
            }
            throw exception;
        }
    }

    public RefreshTokenResponse exchangeRefreshToken(String refreshToken) {
        RefreshTokenRequest requestBody =
                new RefreshTokenRequest(REFRESH_TOKEN_GRANT_TYPE, refreshToken);
        return sendRefreshTokenRequest(requestBody);
    }

    private RefreshTokenResponse sendRefreshTokenRequest(RefreshTokenRequest refreshTokenRequest) {
        try {
            return RestClient.create(REFRESH_TOKEN_BASE_URL)
                    .post()
                    .uri(uriBuilder -> uriBuilder.queryParam(API_KEY_PARAM, webApiKey).build())
                    .body(refreshTokenRequest)
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(RefreshTokenResponse.class);
        } catch (HttpClientErrorException exception) {
            if (exception.getResponseBodyAsString().contains(INVALID_REFRESH_TOKEN_ERROR)) {
                throw new UnauthorizedException("Invalid refresh token");
            }
            throw exception;
        }
    }

}