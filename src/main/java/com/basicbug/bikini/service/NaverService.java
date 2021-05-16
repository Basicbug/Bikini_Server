package com.basicbug.bikini.service;

import com.basicbug.bikini.config.auth.NaverAuthConfig;
import com.basicbug.bikini.dto.auth.NaverProfileResponseDto;
import com.basicbug.bikini.model.CommonConstants;
import com.basicbug.bikini.model.auth.NaverProfile;
import com.basicbug.bikini.model.auth.exception.OAuthProcessException;
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
public class NaverService {

    private final RestTemplate restTemplate;
    private final NaverAuthConfig authConfig;

    public NaverProfile getProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(HttpHeaders.AUTHORIZATION, getBearerString(accessToken));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
        try {
            ResponseEntity<NaverProfileResponseDto> response = restTemplate
                .postForEntity(authConfig.getProfileUrl(), request, NaverProfileResponseDto.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody().getResponse();
            }
        } catch (Exception e) {
            throw new OAuthProcessException("Naver profile request fail");
        }
        throw new OAuthProcessException("Naver profile request fail");
    }

    private String getBearerString(String token) {
        return CommonConstants.BEARER + token;
    }
}
