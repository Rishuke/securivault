package com.esgi.securivault.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorCodeDTO {
    GENERIC_ERROR("GENERIC_ERROR"),
    DATA_NOT_FOUND("DATA_NOT_FOUND"),
    ALREADY_EXIST("ALREADY_EXIST"),
    BAD_REQUEST("BAD_REQUEST"),
    CANT_DELETE_DATA("CANT_DELETE_DATA"),
    NOT_MODIFIED("NOT_MODIFIED"),
    FORBIDDEN("FORBIDDEN"),
    UNAUTHORIZED("UNAUTHORIZED");

    private final String value;

    ErrorCodeDTO(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ErrorCodeDTO fromValue(String value) {
        for (ErrorCodeDTO code : ErrorCodeDTO.values()) {
            if (code.value.equalsIgnoreCase(value)) {
                return code;
            }
        }
        throw new IllegalArgumentException("Unknown error code: " + value);
    }
}
