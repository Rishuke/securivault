package com.esgi.securivault.errors;



import com.esgi.securivault.model.ErrorCodeDTO;
import com.esgi.securivault.model.ErrorDTO;
import org.springframework.http.HttpStatusCode;

import java.util.Optional;

public class HTTPRequestException extends RuntimeException {

    public final ErrorMapping errorCode;
    public final Optional<HttpStatusCode> status;
    public final Optional<String> message;

    public HTTPRequestException(ErrorMapping errorCode, HttpStatusCode status, String message) {
        super(message);
        this.errorCode = errorCode;
        this.status = Optional.of(status);
        this.message = Optional.of(message);
    }

    public HTTPRequestException(ErrorMapping errorCode, HttpStatusCode status) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.status = Optional.of(status);
        message = Optional.empty();
    }

    public HTTPRequestException(ErrorMapping errorCode, String message) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        status = Optional.empty();
        this.message = Optional.of(message);
    }

    public HTTPRequestException(ErrorMapping errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        status = Optional.empty();
        message = Optional.empty();
    }

    public Optional<HttpStatusCode> getHTTPStatusCode() {
        return status;
    }

    public Optional<String> getCustomMessage() {
        return message;
    }

    public ErrorDTO buildErrorDto() {
        ErrorCodeDTO errorCodeDTO = errorCode.getErrorCodeDTO();
        return message
                .map(s -> new ErrorDTO(errorCodeDTO, s))
                .orElseGet(() -> new ErrorDTO(errorCodeDTO, null));
    }
}
