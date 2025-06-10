package com.esgi.securivault.auth.dto;

public record RefreshTokenRequest(String grantType, String refreshToken) {}
