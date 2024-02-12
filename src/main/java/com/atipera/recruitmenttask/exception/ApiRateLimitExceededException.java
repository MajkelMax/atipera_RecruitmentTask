package com.atipera.recruitmenttask.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ApiRateLimitExceededException extends RuntimeException{

    private final int status;
    private final String message;
    public ApiRateLimitExceededException(String errorMessage) {
        super(errorMessage);
        this.status = HttpStatus.FORBIDDEN.value();
        this.message = errorMessage;
    }



}
