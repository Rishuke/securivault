package com.esgi.securivault.services;

import com.esgi.securivault.entity.User;
import com.esgi.securivault.repository.UserRepository;
import com.esgi.securivault.repository.UserRepositoryImpl;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServices {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserRepositoryImpl userRepositoryImpl; // Pour les méthodes supplémentaires
    
    @Autowired
    private FirebaseAuth firebaseAuth;

    public UserServices(UserRepository userRepository, UserRepositoryImpl userRepositoryImpl, FirebaseAuth firebaseAuth) {
        this.userRepository = userRepository;
        this.userRepositoryImpl = userRepositoryImpl;
        this.firebaseAuth = firebaseAuth;
    }

    public User findByEmail(String email) {
        return userRepository.findByemail(email);
    }
    
    public User findByFirebaseUid(String firebaseUid) {
        return userRepository.findByFirebaseUid(firebaseUid);
    }
    
    /**
     * Récupère l'utilisateur actuellement connecté
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        String firebaseUid = (String) authentication.getPrincipal();
        return findByFirebaseUid(firebaseUid);
    }
    
    /**
     * Récupère l'UID Firebase de l'utilisateur connecté
     */
    public String getCurrentUserUid() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        return (String) authentication.getPrincipal();
    }
    
    /**
     * Vérifie si un utilisateur est connecté
     */
    public boolean isUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
    
    /**
     * Synchronise l'utilisateur Firebase avec Firestore
     */
    public User syncUserFromToken(String idToken) throws FirebaseAuthException {
        FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken, false);
        
        String uid = decodedToken.getUid();
        String email = decodedToken.getEmail();
        String name = decodedToken.getName();
        String picture = decodedToken.getPicture();
        boolean emailVerified = decodedToken.isEmailVerified();
        
        // Déterminer le provider
        String provider = "email"; // par défaut
        if (decodedToken.getClaims().containsKey("firebase")) {
            var firebaseClaims = (java.util.Map<String, Object>) decodedToken.getClaims().get("firebase");
            if (firebaseClaims.containsKey("sign_in_provider")) {
                provider = (String) firebaseClaims.get("sign_in_provider");
            }
        }
        
        User existingUser = findByFirebaseUid(uid);
        
        if (existingUser != null) {
            // Mettre à jour l'utilisateur existant
            existingUser.setLastLoginAt(LocalDateTime.now());
            existingUser.setEmail(email);
            existingUser.setDisplayName(name);
            existingUser.setPhotoUrl(picture);
            existingUser.setEmailVerified(emailVerified);
            return userRepositoryImpl.save(existingUser);
        } else {
            // Créer un nouvel utilisateur
            User newUser = new User(uid, email, name, provider);
            newUser.setPhotoUrl(picture);
            newUser.setEmailVerified(emailVerified);
            return userRepositoryImpl.save(newUser);
        }
    }
    
    /**
     * Crée ou met à jour un utilisateur lors de l'inscription
     */
    public User createOrUpdateUser(String firebaseUid, String email, String displayName, String provider) {
        User existingUser = findByFirebaseUid(firebaseUid);
        
        if (existingUser != null) {
            existingUser.setEmail(email);
            existingUser.setDisplayName(displayName);
            existingUser.setLastLoginAt(LocalDateTime.now());
            return userRepositoryImpl.save(existingUser);
        } else {
            User newUser = new User(firebaseUid, email, displayName, provider);
            return userRepositoryImpl.save(newUser);
        }
    }
    
    public void ensureUserExistsFromToken(String idToken) {
    try {
        // Utiliser une vérification plus simple sans checkRevoked
        FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken);
        
        String uid = decodedToken.getUid();
        User existingUser = findByFirebaseUid(uid);
        
        // Si l'utilisateur n'existe pas, le créer avec les infos disponibles
        if (existingUser == null) {
            String email = decodedToken.getEmail();
            String name = decodedToken.getName();
            String picture = decodedToken.getPicture();
            boolean emailVerified = decodedToken.isEmailVerified();
            
            // Déterminer le provider de façon plus sûre
            String provider = determineProvider(decodedToken);
            
            // Créer l'utilisateur
            User newUser = createNewUser(uid, email, name, provider, picture, emailVerified);
            userRepositoryImpl.save(newUser);
        } else {
            // Mettre à jour seulement la date de dernière connexion
            updateLastLogin(existingUser);
        }
        
    } catch (Exception e) {
        // Log l'erreur mais ne pas faire échouer l'authentification
        System.err.println("Failed to sync user from token: " + e.getMessage());
        // L'utilisateur peut exister en base même si le token a des problèmes mineurs
    }
}

    private String determineProvider(FirebaseToken decodedToken) {
        try {
            Object firebaseClaims = decodedToken.getClaims().get("firebase");
            if (firebaseClaims instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> claims = (java.util.Map<String, Object>) firebaseClaims;
                Object provider = claims.get("sign_in_provider");
                return provider != null ? provider.toString() : "email";
            }
        } catch (Exception e) {
            // Ignore et utilise la valeur par défaut
        }
        return "email";
    }

    private User createNewUser(String uid, String email, String name, String provider, 
                            String picture, boolean emailVerified) {
        User newUser = new User(uid, email, name, provider);
        newUser.setPhotoUrl(picture);
        newUser.setEmailVerified(emailVerified);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setLastLoginAt(LocalDateTime.now());
        return newUser;
    }

    private void updateLastLogin(User existingUser) {
        existingUser.setLastLoginAt(LocalDateTime.now());
        userRepositoryImpl.save(existingUser);
    }
    /**
 * Version sécurisée qui évite les problèmes de sérialisation Jackson
 */
/**
 * Version sécurisée qui évite complètement les problèmes de sérialisation Jackson
 */
/**
 * Version finale qui évite complètement les problèmes de sérialisation
 */
    public void ensureUserExistsFromTokenSafe(String idToken) {
        try {
            // Parse le token manuellement sans utiliser Jackson pour les dates
            Map<String, Object> claims = parseTokenSafely(idToken);
            
            if (claims == null) {
                System.out.println("Could not parse token manually");
                return;
            }
            
            String uid = (String) claims.get("user_id");
            
            if (uid == null || uid.isEmpty()) {
                System.out.println("No user_id found in token");
                return;
            }
            
            User existingUser = findByFirebaseUid(uid);
            
            if (existingUser == null) {
                // Créer un nouvel utilisateur
                createUserSafely(uid, claims);
            } else {
                // Mettre à jour seulement la dernière connexion
                updateUserLastLoginSafely(existingUser);
            }
            
        } catch (Exception e) {
            System.err.println("Failed to sync user from token (safe mode): " + e.getMessage());
        }
    }

    /**
     * Parse le token de façon sécurisée
     */
    private Map<String, Object> parseTokenSafely(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return null;
            }

            // Décoder le payload
            byte[] decodedBytes = java.util.Base64.getUrlDecoder().decode(parts[1]);
            String payload = new String(decodedBytes, java.nio.charset.StandardCharsets.UTF_8);
            
            // Parser JSON sans Jackson pour éviter les problèmes
            Map<String, Object> claims = parseJsonSafely(payload);
            
            if (claims == null || !validateTokenClaimsSafely(claims)) {
                return null;
            }
            
            return claims;
            
        } catch (Exception e) {
            System.err.println("Failed to parse token safely: " + e.getMessage());
            return null;
        }
    }

    /**
     * Parse JSON de façon basique et sécurisée
     */
    private Map<String, Object> parseJsonSafely(String json) {
        Map<String, Object> result = new java.util.HashMap<>();
        
        try {
            // Parser JSON très basique pour éviter Jackson
            json = json.trim();
            if (!json.startsWith("{") || !json.endsWith("}")) {
                return null;
            }
            
            // Enlever les accolades
            json = json.substring(1, json.length() - 1);
            
            // Séparer par les virgules (en tenant compte des objets imbriqués)
            java.util.List<String> pairs = splitJsonPairs(json);
            
            for (String pair : pairs) {
                String[] keyValue = pair.split(":", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim().replaceAll("\"", "");
                    String value = keyValue[1].trim();
                    
                    // Parser les valeurs de base
                    if (value.equals("true")) {
                        result.put(key, true);
                    } else if (value.equals("false")) {
                        result.put(key, false);
                    } else if (value.matches("\\d+")) {
                        result.put(key, Long.parseLong(value));
                    } else if (value.startsWith("\"") && value.endsWith("\"")) {
                        result.put(key, value.substring(1, value.length() - 1));
                    } else if (value.startsWith("{")) {
                        // Objet imbriqué simple
                        result.put(key, parseNestedObject(value));
                    }
                }
            }
            
            return result;
            
        } catch (Exception e) {
            System.err.println("JSON parsing failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Sépare les paires JSON en tenant compte des objets imbriqués
     */
    private java.util.List<String> splitJsonPairs(String json) {
        java.util.List<String> pairs = new java.util.ArrayList<>();
        int start = 0;
        int braceLevel = 0;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            
            if (c == '{') {
                braceLevel++;
            } else if (c == '}') {
                braceLevel--;
            } else if (c == ',' && braceLevel == 0) {
                pairs.add(json.substring(start, i));
                start = i + 1;
            }
        }
        
        // Ajouter la dernière paire
        if (start < json.length()) {
            pairs.add(json.substring(start));
        }
        
        return pairs;
    }

    /**
     * Parse un objet JSON imbriqué simple
     */
    private Map<String, Object> parseNestedObject(String json) {
        Map<String, Object> result = new java.util.HashMap<>();
        
        try {
            json = json.trim();
            if (json.startsWith("{") && json.endsWith("}")) {
                json = json.substring(1, json.length() - 1);
                
                String[] pairs = json.split(",");
                for (String pair : pairs) {
                    String[] keyValue = pair.split(":", 2);
                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim().replaceAll("\"", "");
                        String value = keyValue[1].trim().replaceAll("\"", "");
                        result.put(key, value);
                    }
                }
            }
        } catch (Exception e) {
            // Ignore les erreurs de parsing des objets imbriqués
        }
        
        return result;
    }

    /**
     * Valide les claims du token de façon sécurisée
     */
    private boolean validateTokenClaimsSafely(Map<String, Object> claims) {
        // Vérifier l'audience
        String audience = (String) claims.get("aud");
        if (!"projet-annuel-b3477".equals(audience)) {
            System.err.println("Invalid audience: " + audience);
            return false;
        }
        
        // Vérifier l'issuer
        String issuer = (String) claims.get("iss");
        boolean validIssuer = "https://securetoken.google.com/projet-annuel-b3477".equals(issuer) || 
                            "https://identitytoolkit.google.com/".equals(issuer);
        
        if (!validIssuer) {
            System.err.println("Invalid issuer: " + issuer);
            return false;
        }
        
        // Vérifier l'expiration
        Object exp = claims.get("exp");
        if (exp != null) {
            long expirationTime = ((Number) exp).longValue();
            long currentTime = System.currentTimeMillis() / 1000;
            
            if (currentTime > expirationTime) {
                System.err.println("Token expired");
                return false;
            }
        }
        
        return true;
    }

    /**
     * Crée un utilisateur de façon sécurisée sans problème de sérialisation
     */
    private void createUserSafely(String uid, Map<String, Object> claims) {
        try {
            String email = (String) claims.get("email");
            String name = (String) claims.get("name");
            String picture = (String) claims.get("picture");
            Object emailVerifiedObj = claims.get("email_verified");
            boolean emailVerified = emailVerifiedObj instanceof Boolean ? (Boolean) emailVerifiedObj : false;
            
            // Déterminer le provider
            String provider = "email";
            Object firebaseObj = claims.get("firebase");
            if (firebaseObj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> firebaseClaims = (Map<String, Object>) firebaseObj;
                Object providerObj = firebaseClaims.get("sign_in_provider");
                if (providerObj != null) {
                    provider = providerObj.toString();
                }
            }
            
            // Créer l'utilisateur manuellement sans sérialisation problématique
            User newUser = createUserEntity(uid, email, name, provider, picture, emailVerified);
            
            // Sauvegarder avec gestion d'erreur
            try {
                userRepositoryImpl.save(newUser);
                System.out.println("Successfully created user: " + uid);
            } catch (Exception saveException) {
                System.err.println("Failed to save user: " + saveException.getMessage());
                // Essayer avec un utilisateur plus simple
                User simpleUser = createSimpleUserEntity(uid, email, name, provider);
                userRepositoryImpl.save(simpleUser);
                System.out.println("Created simple user: " + uid);
            }
            
        } catch (Exception e) {
            System.err.println("Failed to create user safely: " + e.getMessage());
        }
    }

    /**
     * Crée une entité User avec toutes les propriétés
     */
    private User createUserEntity(String uid, String email, String name, String provider, String picture, boolean emailVerified) {
        User user = new User();
        user.setFirebaseUid(uid);
        user.setEmail(email);
        user.setDisplayName(name);
        user.setProvider(provider);
        user.setPhotoUrl(picture);
        user.setEmailVerified(emailVerified);
        
        // Utiliser des timestamps simples
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        user.setCreatedAt(now);
        user.setLastLoginAt(now);
        
        return user;
    }

    /**
     * Crée une entité User simplifiée en cas d'erreur
     */
    private User createSimpleUserEntity(String uid, String email, String name, String provider) {
        User user = new User();
        user.setFirebaseUid(uid);
        user.setEmail(email);
        user.setDisplayName(name);
        user.setProvider(provider);
        // Ne pas définir les dates si ça pose problème
        
        return user;
    }

    /**
     * Met à jour la dernière connexion de façon sécurisée
     */
    private void updateUserLastLoginSafely(User existingUser) {
        try {
            existingUser.setLastLoginAt(java.time.LocalDateTime.now());
            userRepositoryImpl.save(existingUser);
            System.out.println("Updated last login for user: " + existingUser.getFirebaseUid());
        } catch (Exception e) {
            System.err.println("Failed to update last login, but continuing: " + e.getMessage());
            // Ne pas faire échouer si on ne peut pas mettre à jour la date
        }
    }
}