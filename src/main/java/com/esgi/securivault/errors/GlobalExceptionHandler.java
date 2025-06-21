package com.esgi.securivault.errors;

import com.esgi.securivault.model.ErrorCodeDTO;
import com.esgi.securivault.model.ErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDTO handleValidationErrors(MethodArgumentNotValidException e) {
        return new ErrorDTO(ErrorCodeDTO.BAD_REQUEST, e.getBindingResult().getFieldErrors().getFirst().getDefaultMessage());
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorDTO handle(Exception e) {
        this.logger.error("Erreur serveur", e);
        return new ErrorDTO(ErrorCodeDTO.GENERIC_ERROR, "Une erreur interne est survenue");
    }
}

