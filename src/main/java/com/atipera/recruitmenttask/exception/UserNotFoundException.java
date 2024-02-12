package com.atipera.recruitmenttask.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException{

    private final int status;
    private final String message;

    public UserNotFoundException(String errorMessage) {
        super(errorMessage);
        this.status = HttpStatus.NOT_FOUND.value();
        this.message = errorMessage;
    }


}
