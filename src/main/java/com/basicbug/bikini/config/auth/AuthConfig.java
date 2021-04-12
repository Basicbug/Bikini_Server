package com.basicbug.bikini.config.auth;

public interface AuthConfig {
    String getBaseUrl();
    String getProfileUrl();
    String getTokenUrl();
    String getClientId();
    String getClientSecret();
}
