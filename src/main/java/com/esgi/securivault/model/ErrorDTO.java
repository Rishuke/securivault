package com.esgi.securivault.model;

public class ErrorDTO {
    private final ErrorCodeDTO code;
    private final String message;

    public ErrorDTO(ErrorCodeDTO code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorCodeDTO getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
