package com.esgi.securivault.errors;


import com.esgi.securivault.model.ErrorCodeDTO;
import com.esgi.securivault.model.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public enum ErrorMapping {
    GENERIC_ERROR(
            ErrorCodeDTO.GENERIC_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error happened"),
    DATA_NOT_FOUND(
            ErrorCodeDTO.DATA_NOT_FOUND, HttpStatus.NOT_FOUND, "The requested data has not been found"),
    ALREADY_EXIST(
            ErrorCodeDTO.ALREADY_EXIST,
            HttpStatus.CONFLICT,
            "The requested entity has already been created"),
    BAD_REQUEST(
            ErrorCodeDTO.BAD_REQUEST, HttpStatus.BAD_REQUEST, "The request contains invalid parameters"),
    CANT_DELETE_DATA(
            ErrorCodeDTO.CANT_DELETE_DATA, HttpStatus.CONFLICT, "This data can't be deleted"),
    NOT_MODIFIED(
            ErrorCodeDTO.NOT_MODIFIED, HttpStatus.NOT_MODIFIED, "This data hasn't been modified"),
    UNAUTHORIZED(
            ErrorCodeDTO.UNAUTHORIZED, HttpStatus.UNAUTHORIZED, "You are not authorized to access this resource"),;

    private final ErrorCodeDTO errorCodeDTO;
    private final String defaultMessage;
    private final HttpStatusCode status;

    ErrorMapping(ErrorCodeDTO errorCodeDTO, HttpStatusCode status, String defaultMessage) {
        this.errorCodeDTO = errorCodeDTO;
        this.defaultMessage = defaultMessage;
        this.status = status;
    }

    public HttpStatusCode getStatus() {
        return status;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public ErrorCodeDTO getErrorCodeDTO() {
        return errorCodeDTO;
    }

    public ResponseEntity<ErrorDTO> buildDefaultResponseEntity() {
        return ResponseEntity.status(status).body(new ErrorDTO(errorCodeDTO, defaultMessage));
    }
}