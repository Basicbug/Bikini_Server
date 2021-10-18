package com.basicbug.bikini.auth.dto;

import com.basicbug.bikini.auth.model.OAuthToken;
import lombok.Value;

@Value
public class JwtTokenResponseDto {
    String accessToken;
    String refreshToken;

    public JwtTokenResponseDto(OAuthToken oAuthToken) {
        this.accessToken = oAuthToken.getAccessToken();
        this.refreshToken = oAuthToken.getRefreshToken();
    }
}
