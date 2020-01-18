package com.space.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
ошибка запроса параметра
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException{
    public BadRequestException() {
    }

    public BadRequestException(String s) {
        super(s);
    }

    public BadRequestException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public BadRequestException(Throwable throwable) {
        super(throwable);
    }
}
