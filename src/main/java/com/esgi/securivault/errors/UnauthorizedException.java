package com.esgi.securivault.errors;

public class UnauthorizedException extends HTTPRequestException {

    public UnauthorizedException(String message) {
        super(ErrorMapping.UNAUTHORIZED, message);
    }

    public UnauthorizedException() {
        super(ErrorMapping.UNAUTHORIZED);
    }
}
