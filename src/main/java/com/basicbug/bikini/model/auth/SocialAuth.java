package com.basicbug.bikini.model.auth;

public abstract class SocialAuth {
    private String token_type;
    private String access_token;
    private String refresh_token;
    private Long expires_in;
}
