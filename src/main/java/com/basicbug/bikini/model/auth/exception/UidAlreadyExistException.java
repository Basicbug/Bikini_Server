package com.basicbug.bikini.model.auth.exception;

public class UidAlreadyExistException extends RuntimeException {

    public UidAlreadyExistException(String message) {
        super(message);
    }
}
