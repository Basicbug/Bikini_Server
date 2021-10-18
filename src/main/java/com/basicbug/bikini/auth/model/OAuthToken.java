package com.basicbug.bikini.auth.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OAuthToken {
    String accessToken;
    String refreshToken;
}
