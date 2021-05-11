package com.basicbug.bikini.model.auth.exception;

public class InvalidAccessTokenException extends IllegalArgumentException {
    public InvalidAccessTokenException(String message) {
        super(message);
    }
}
