package com.basicbug.bikini.user.exception;

public class RefreshTokenNotMatchedException extends RuntimeException {
    public RefreshTokenNotMatchedException(String message) {
        super(message);
    }
}
