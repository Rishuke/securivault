package com.esgi.securivault.errors;

public class HTTPRequestException extends RuntimeException {
    private final ErrorMapping errorMapping;

    public HTTPRequestException(ErrorMapping errorMapping, String message) {
        super(message);
        this.errorMapping = errorMapping;
    }

    public HTTPRequestException(ErrorMapping errorMapping) {
        this(errorMapping, errorMapping.getMessage());
    }

    public ErrorMapping getErrorMapping() {
        return errorMapping;
    }
}