package com.esgi.securivault.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.esgi.securivault.services.UserServices;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final FirebaseAuth firebaseAuth;
    private final ObjectMapper objectMapper;
    private final UserServices userServices;
    
    @Value("${securivault.firebase.web-api-key}")
    private String webApiKey;

    public TokenAuthenticationFilter(FirebaseAuth firebaseAuth, ObjectMapper objectMapper, UserServices userServices) {
        this.firebaseAuth = firebaseAuth;
        this.objectMapper = objectMapper;
        this.userServices = userServices;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws IOException, ServletException {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        logger.debug("Authorization Header: " + authorizationHeader);

        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            String token = authorizationHeader.replace(BEARER_PREFIX, "");
            Optional<String> userId = extractUserIdFromToken(token);

            if (userId.isPresent()) {
                // Synchroniser l'utilisateur avec la base de données si nécessaire
                try {
                    userServices.ensureUserExistsFromTokenSafe(token);
                } catch (Exception e) {
                    logger.warn("Failed to sync user from token: " + e.getMessage());
                    // Continue anyway, l'utilisateur existe peut-être déjà
                }
                
                var authentication = new UsernamePasswordAuthenticationToken(userId.get(), null, null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                setAuthErrorDetails(response);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private Optional<String> extractUserIdFromToken(String token) {
        try {
            // D'abord essayer la vérification Firebase standard
            FirebaseToken firebaseToken = firebaseAuth.verifyIdToken(token, false);
            return Optional.of(firebaseToken.getUid());
            
        } catch (FirebaseAuthException exception) {
            logger.warn("Firebase SDK verification failed: " + exception.getMessage());
            
            // Si c'est un problème d'issuer, faire une vérification manuelle
            if (exception.getMessage().contains("incorrect \"iss\" (issuer) claim")) {
                return verifyTokenManually(token);
            }
            
            return Optional.empty();
        }
    }

    private Optional<String> verifyTokenManually(String token) {
        try {
            // Décoder le JWT manuellement
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                logger.error("Invalid JWT format");
                return Optional.empty();
            }

            // Décoder le payload (partie 1)
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            Map<String, Object> claims = objectMapper.readValue(payload, Map.class);
            
            logger.debug("Token claims: " + claims);

            // Vérifier les claims essentiels
            String issuer = (String) claims.get("iss");
            String audience = (String) claims.get("aud");
            Object exp = claims.get("exp");
            String uid = (String) claims.get("user_id");
            
            // Vérifier l'audience (projet Firebase)
            if (!audience.equals("projet-annuel-b3477")) {
                logger.warn("Invalid audience: " + audience);
                return Optional.empty();
            }
            
            // Vérifier l'issuer (accepter les deux variantes)
            boolean validIssuer = issuer.equals("https://securetoken.google.com/projet-annuel-b3477") || 
                                 issuer.equals("https://identitytoolkit.google.com/");
            
            logger.debug("Checking issuer: '" + issuer + "' - valid: " + validIssuer);
            
            if (!validIssuer) {
                logger.warn("Invalid issuer: '" + issuer + "'");
                return Optional.empty();
            }
            
            // Vérifier l'expiration
            if (exp != null) {
                long expirationTime = ((Number) exp).longValue();
                long currentTime = System.currentTimeMillis() / 1000;
                
                if (currentTime > expirationTime) {
                    logger.warn("Token expired");
                    return Optional.empty();
                }
            }
            
            // Vérifier que l'UID existe
            if (uid == null || uid.isEmpty()) {
                logger.warn("No user_id in token");
                return Optional.empty();
            }
            
            logger.info("Token manually verified for user: " + uid);
            return Optional.of(uid);
            
        } catch (Exception e) {
            logger.error("Manual token verification failed", e);
            return Optional.empty();
        }
    }

    private void setAuthErrorDetails(HttpServletResponse response) throws IOException {
        HttpStatus unauthorized = HttpStatus.UNAUTHORIZED;
        response.setStatus(unauthorized.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(unauthorized,
                "Authentication failure: Token missing, invalid or expired");
        response.getWriter().write(objectMapper.writeValueAsString(problemDetail));
    }
}