package com.atipera.recruitmenttask.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RepositoryNotFoundException extends RuntimeException{

    public RepositoryNotFoundException(String errorMessage) {
        super(errorMessage);
    }

}
