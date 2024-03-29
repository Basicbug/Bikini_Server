package com.basicbug.bikini.auth.controller;

import static com.basicbug.bikini.common.type.CommonResponseCode.SUCCESS;

import com.basicbug.bikini.auth.dto.AuthRequestDto;
import com.basicbug.bikini.auth.dto.JwtTokenRefreshRequestDto;
import com.basicbug.bikini.auth.dto.JwtTokenResponseDto;
import com.basicbug.bikini.auth.model.AuthProvider;
import com.basicbug.bikini.auth.model.KakaoAuth;
import com.basicbug.bikini.auth.model.NaverAuth;
import com.basicbug.bikini.auth.model.OAuthToken;
import com.basicbug.bikini.auth.model.SocialAuth;
import com.basicbug.bikini.common.dto.CommonResponse;
import com.basicbug.bikini.config.auth.AuthConfig;
import com.basicbug.bikini.config.auth.KakaoAuthConfig;
import com.basicbug.bikini.config.auth.NaverAuthConfig;
import com.basicbug.bikini.user.service.UserService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

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

    @GetMapping("/login/{provider}")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<JwtTokenResponseDto> login(@PathVariable("provider") String provider, AuthRequestDto requestDto) {
        AuthProvider authProvider = AuthProvider.of(provider.toUpperCase());
        OAuthToken oAuthToken = userService.checkOrRegisterUser(requestDto, authProvider);
        return CommonResponse.of(new JwtTokenResponseDto(oAuthToken), SUCCESS);
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<JwtTokenResponseDto> refresh(JwtTokenRefreshRequestDto requestDto) {
        OAuthToken oAuthToken = userService.refreshToken(requestDto);
        return CommonResponse.of(new JwtTokenResponseDto(oAuthToken), SUCCESS);
    }

    @ApiIgnore
    @GetMapping("/test/login")
    public ModelAndView testLoginPage(ModelAndView modelAndView) {
        String naverRedirect = "http://ec2-3-34-36-203.ap-northeast-2.compute.amazonaws.com:8080/v1/auth/redirect/naver";
        String kakaoRedirect = "http://ec2-3-34-36-203.ap-northeast-2.compute.amazonaws.com:8080/v1/auth/redirect/kakao";

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

    @ApiIgnore
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
}
