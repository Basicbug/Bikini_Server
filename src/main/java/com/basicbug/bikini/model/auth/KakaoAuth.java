package com.basicbug.bikini.model.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoAuth extends SocialAuth {
    private Long refresh_token_expires_in;
    private String scope;
}
