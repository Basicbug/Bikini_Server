package com.basicbug.bikini.config.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class NaverAuthConfig implements AuthConfig {

    @Value("${spring.social.naver.url.login}")
    private String baseUrl;

    @Value("${spring.social.naver.url.profile}")
    private String profileUrl;

    @Value("${spring.social.naver.url.token}")
    private String tokenUrl;

    @Value("${spring.social.naver.client_id}")
    private String clientId;

    @Value("${spring.social.naver.client_secret}")
    private String clientSecret;
}
