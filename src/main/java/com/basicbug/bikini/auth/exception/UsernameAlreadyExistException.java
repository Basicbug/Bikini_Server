package com.basicbug.bikini.auth.exception;

public class UsernameAlreadyExistException extends RuntimeException {

    public UsernameAlreadyExistException(String message) {
        super(message);
    }
}
