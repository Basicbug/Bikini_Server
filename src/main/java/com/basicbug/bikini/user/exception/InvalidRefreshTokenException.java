package com.basicbug.bikini.user.exception;

public class InvalidRefreshTokenException extends IllegalArgumentException {
    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}
