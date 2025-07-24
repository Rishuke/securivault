package com.esgi.securivault.controller;

import com.esgi.securivault.entity.User;
import com.esgi.securivault.services.UserServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServices userServices;

    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @GetMapping("/by-email")
    public User getUserByEmail(@RequestParam String email) {
        return userServices.findByEmail(email);
    }
    
    /**
     * Récupère le profil de l'utilisateur connecté
     */
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUserProfile() {
        if (!userServices.isUserAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        
        User currentUser = userServices.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.notFound().build();
        }
        
        UserProfileResponse profile = new UserProfileResponse(
            currentUser.getFirebaseUid(),
            currentUser.getEmail(),
            currentUser.getDisplayName(),
            currentUser.getPhotoUrl(),
            currentUser.getProvider(),
            currentUser.isEmailVerified(),
            currentUser.getCreatedAt(),
            currentUser.getLastLoginAt()
        );
        
        return ResponseEntity.ok(profile);
    }
    
    /**
     * Vérifie si l'utilisateur est connecté
     */
    @GetMapping("/auth-status")
    public ResponseEntity<AuthStatusResponse> getAuthStatus() {
        boolean isAuthenticated = userServices.isUserAuthenticated();
        String userUid = userServices.getCurrentUserUid();
        
        return ResponseEntity.ok(new AuthStatusResponse(isAuthenticated, userUid));
    }
    
    /**
     * Recherche un utilisateur par email (pour ajouter à une valise)
     */
    @GetMapping("/search")
    public ResponseEntity<UserSearchResponse> searchUserByEmail(@RequestParam String email) {
        if (!userServices.isUserAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        
        User user = userServices.findByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Retourner seulement les infos publiques
        UserSearchResponse response = new UserSearchResponse(
            user.getFirebaseUid(),
            user.getEmail(),
            user.getDisplayName(),
            user.getPhotoUrl()
        );
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Méthode de debug pour analyser le token
     */
    @GetMapping("/debug-token")
    public ResponseEntity<Map<String, Object>> debugToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        Map<String, Object> debug = new HashMap<>();
        
        debug.put("authHeader", authHeader);
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", "");
            debug.put("tokenLength", token.length());
            debug.put("tokenStart", token.length() > 50 ? token.substring(0, 50) + "..." : token);
            
            String[] parts = token.split("\\.");
            debug.put("tokenParts", parts.length);
            
            if (parts.length >= 2) {
                try {
                    byte[] decodedBytes = java.util.Base64.getUrlDecoder().decode(parts[1]);
                    String payload = new String(decodedBytes, java.nio.charset.StandardCharsets.UTF_8);
                    debug.put("payload", payload);
                } catch (Exception e) {
                    debug.put("payloadError", e.getMessage());
                }
            }
        } else {
            debug.put("error", "No Bearer token found");
        }
        
        return ResponseEntity.ok(debug);
    }
    
    // DTOs pour les réponses
    public record UserProfileResponse(
        String uid,
        String email,
        String displayName,
        String photoUrl,
        String provider,
        boolean emailVerified,
        java.time.LocalDateTime createdAt,
        java.time.LocalDateTime lastLoginAt
    ) {}
    
    public record AuthStatusResponse(
        boolean isAuthenticated,
        String userUid
    ) {}
    
    public record UserSearchResponse(
        String uid,
        String email,
        String displayName,
        String photoUrl
    ) {}
}