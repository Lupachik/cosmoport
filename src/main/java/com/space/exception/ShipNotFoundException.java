package com.space.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
ошибка если корабль не найден
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ShipNotFoundException extends RuntimeException {
    public ShipNotFoundException() {
    }

    public ShipNotFoundException(String s) {
        super(s);
    }

    public ShipNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ShipNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
