package com.basicbug.bikini.auth.exception;

public class InvalidProviderException extends IllegalArgumentException {
    public InvalidProviderException(String message) {
        super(message);
    }
}
