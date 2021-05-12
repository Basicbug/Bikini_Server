package com.basicbug.bikini.model.auth.exception;

public class InvalidProviderException extends IllegalArgumentException {
    public InvalidProviderException(String message) {
        super(message);
    }
}
