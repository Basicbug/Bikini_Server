package com.basicbug.bikini.auth.exception;

public class InvalidAccessTokenException extends IllegalArgumentException {
    public InvalidAccessTokenException(String message) {
        super(message);
    }
}
