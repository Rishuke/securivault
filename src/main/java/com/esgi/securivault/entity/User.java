package com.esgi.securivault.entity;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.firebase.database.annotations.Nullable;
import java.time.LocalDateTime;

public class User {
    
    @DocumentId
    private String firebaseUid; // Utiliser l'UID Firebase comme ID principal
    
    private String email;
    private String displayName;
    private String photoUrl;
    private String provider; // "email", "google", etc.
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
    private boolean emailVerified;
    
    // Garder l'ancien userId pour compatibilité si nécessaire
    @Nullable 
    private Long userId; // Optionnel, pour migration
    
    // Ne plus stocker le password car Firebase s'en occupe
    // private String password; // Supprimé car géré par Firebase Auth

    // Constructeurs
    public User() {}
    
    public User(String firebaseUid, String email, String displayName, String provider) {
        this.firebaseUid = firebaseUid;
        this.email = email;
        this.displayName = displayName;
        this.provider = provider;
        this.createdAt = LocalDateTime.now();
        this.lastLoginAt = LocalDateTime.now();
        this.emailVerified = false;
    }

    // Getters et Setters
    public String getFirebaseUid() { return firebaseUid; }
    public void setFirebaseUid(String firebaseUid) { this.firebaseUid = firebaseUid; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }
    
    public boolean isEmailVerified() { return emailVerified; }
    public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }
    
    // Pour compatibilité avec l'ancien système
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}