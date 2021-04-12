package com.basicbug.bikini.config.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class KakaoAuthConfig implements AuthConfig {

    @Value("${spring.social.kakao.url.login}")
    private String baseUrl;

    @Value("${spring.social.kakao.url.profile}")
    private String profileUrl;

    @Value("${spring.social.kakao.url.token}")
    private String tokenUrl;

    @Value("${spring.social.kakao.client_id}")
    private String clientId;

    @Value("${spring.social.kakao.client_secret}")
    private String clientSecret;
}
