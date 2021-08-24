package com.basicbug.bikini.model.auth.exception;

public class UsernameAlreadyExistException extends RuntimeException {

    public UsernameAlreadyExistException(String message) {
        super(message);
    }
}
