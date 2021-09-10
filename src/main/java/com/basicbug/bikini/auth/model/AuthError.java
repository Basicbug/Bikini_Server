package com.basicbug.bikini.auth.model;

public enum AuthError {

    INVALID_PROVIDER(1000, "Invalid provider"),
    INVALID_ACCESS_TOKEN(2000, "Invalid access token");

    public final int errorCode;
    public final String errorMsg;

    AuthError(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
