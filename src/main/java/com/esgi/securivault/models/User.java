package com.esgi.securivault.models;

import com.google.cloud.Timestamp;

public class User {
    private String uid;
    private String email;
    private String password;
    private Timestamp createdAt;
    private Timestamp lastLogin;

    // Constructeur par défaut nécessaire pour Firebase
    public User() {}

    // Getters et Setters
    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }
}