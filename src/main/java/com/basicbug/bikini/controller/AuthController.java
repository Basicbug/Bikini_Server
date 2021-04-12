package com.basicbug.bikini.controller;

import com.basicbug.bikini.config.auth.AuthConfig;
import com.basicbug.bikini.config.auth.KakaoAuthConfig;
import com.basicbug.bikini.config.auth.NaverAuthConfig;
import com.basicbug.bikini.dto.auth.NaverAuthRequestDto;
import com.basicbug.bikini.dto.common.CommonResponse;
import com.basicbug.bikini.model.auth.AuthProvider;
import com.basicbug.bikini.model.auth.KakaoAuth;
import com.basicbug.bikini.model.auth.NaverAuth;
import com.basicbug.bikini.model.auth.SocialAuth;
import com.basicbug.bikini.service.UserService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RestTemplate restTemplate;

    private final Gson gson;

    private final UserService userService;

    private final NaverAuthConfig naverAuthConfig;

    private final KakaoAuthConfig kakaoAuthConfig;

    @GetMapping("/login/naver")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<String> login(NaverAuthRequestDto naverAuthRequestDto) {
        String jwtToken = userService.login(naverAuthRequestDto);

        if (jwtToken.isEmpty()) {
            AuthError error = AuthError.INVALID_PARAM;
            return CommonResponse.error(error.errorCode, error.errorMsg);
        } else {
            return CommonResponse.of(jwtToken, "200", "Success");
        }
    }

    @GetMapping("/test/login")
    public ModelAndView testLoginPage(ModelAndView modelAndView) {
        String naverRedirect = "http://localhost:8080/v1/auth/redirect/naver";
        String kakaoRedirect = "http://localhost:8080/v1/auth/redirect/kakao";

        String naverLoginUrl = getLoginUrl(naverAuthConfig, naverRedirect);
        String kakaoLoginUrl = getLoginUrl(kakaoAuthConfig, kakaoRedirect);

        modelAndView.addObject("naverLoginUrl", naverLoginUrl);
        modelAndView.addObject("kakaoLoginUrl", kakaoLoginUrl);
        modelAndView.setViewName("auth/login");
        return modelAndView;
    }

    private String getLoginUrl(AuthConfig authConfig, String redirectUri) {
        StringBuilder loginUrl = new StringBuilder(authConfig.getBaseUrl());
        loginUrl.append("?client_id=").append(authConfig.getClientId())
            .append("&redirect_uri=").append(redirectUri)
            .append("&response_type=code");

        return loginUrl.toString();
    }

    @GetMapping("/redirect/{provider}")
    public ModelAndView redirectNaverLogin(ModelAndView modelAndView, @RequestParam String code, @PathVariable String provider) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        AuthProvider authProvider = AuthProvider.valueOf(provider.toUpperCase());
        AuthConfig authConfig = getServiceAuthConfig(authProvider);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", authConfig.getClientId());
        params.add("client_secret", authConfig.getClientSecret());
        params.add("redirect_uri", "http://localhost:8080/v1/auth/redirect/" + authProvider.getName());
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(authConfig.getTokenUrl(), request, String.class);

        SocialAuth result = null;

        if (response.getStatusCode() == HttpStatus.OK) {
            result = getAuthResponse(authProvider, response.getBody());
        }

        modelAndView.addObject("authInfo", result);
        modelAndView.setViewName("auth/redirect");
        return modelAndView;
    }

    private SocialAuth getAuthResponse(AuthProvider provider, String body) {
        if (provider == AuthProvider.KAKAO) {
            return gson.fromJson(body, KakaoAuth.class);
        } else {
            return gson.fromJson(body, NaverAuth.class);
        }
    }

    private AuthConfig getServiceAuthConfig(AuthProvider provider) {
        if (provider == AuthProvider.KAKAO) {
            return kakaoAuthConfig;
        } else {
            return naverAuthConfig;
        }
    }

    enum AuthError {

        INVALID_PARAM("1000", "Invalid param");

        String errorCode;
        String errorMsg;

        AuthError(String errorCode, String errorMsg) {
            this.errorCode = errorCode;
            this.errorMsg = errorMsg;
        }
    }
}
