package com.groupe.gestion_clinic.handler;

import com.groupe.gestion_clinic.exceptions.ObjectValidationException;
import com.groupe.gestion_clinic.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handlerException(UserNotFoundException exception) {

        final HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse errorResponse =
                                    ErrorResponse
                                            .builder()
                                            .message(exception.getMessage())
                                            .status(status.value())
                                            .build();
        return ResponseEntity.ok(errorResponse);

    }

    @ExceptionHandler(ObjectValidationException.class)
    public ResponseEntity<?> handlerException(ObjectValidationException exception) {

        final HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        ErrorResponse errorResponse =
                                        ErrorResponse
                                                .builder()
                                                .message(exception.getMessage())
                                                .msgViolations(exception.getViolations())
                                                .status(status.value())
                                                .build();
        return ResponseEntity.ok(errorResponse);

    }
}
