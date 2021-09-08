package com.basicbug.bikini.auth.service;

import com.basicbug.bikini.config.auth.KakaoAuthConfig;
import com.basicbug.bikini.auth.model.KakaoProfile;
import com.basicbug.bikini.common.constant.CommonConstants;
import com.basicbug.bikini.auth.exception.OAuthProcessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final RestTemplate restTemplate;
    private final KakaoAuthConfig authConfig;

    public KakaoProfile getProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(HttpHeaders.AUTHORIZATION, getBearerString(accessToken));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
        try {
            ResponseEntity<KakaoProfile> response = restTemplate
                .postForEntity(authConfig.getProfileUrl(), request, KakaoProfile.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
        } catch (Exception e) {
            throw new OAuthProcessException("Kakao profile request fail");
        }
        throw new OAuthProcessException("Kakao profile request fail");
    }

    private String getBearerString(String token) {
        return CommonConstants.BEARER + token;
    }
}
