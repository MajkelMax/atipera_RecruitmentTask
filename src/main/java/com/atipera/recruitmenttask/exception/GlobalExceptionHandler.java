package com.atipera.recruitmenttask.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorMessage errorMessage = new ErrorMessage(status.value(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, status);
    }

    @ExceptionHandler(RepositoryNotFoundException.class)
    public ResponseEntity<Object> handleRepositoryNotFoundException(RepositoryNotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorMessage errorMessage = new ErrorMessage(status.value(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, status);
    }

    @ExceptionHandler(ApiRateLimitExceededException.class)
    public ResponseEntity<Object> handleApiRateLimitExceededException(ApiRateLimitExceededException ex) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        ErrorMessage errorMessage = new ErrorMessage(status.value(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, status);
    }

    @Getter
    static class ErrorMessage {
        private final int status;
        private final String message;

        public ErrorMessage(int status, String message) {
            this.status = status;
            this.message = message;
        }

    }

}
