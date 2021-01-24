package com.basicbug.bikini.controller;

import com.basicbug.bikini.dto.auth.NaverAuthRequestDto;
import com.basicbug.bikini.dto.auth.NaverProfileResponseDto;
import com.basicbug.bikini.dto.common.CommonResponse;
import com.basicbug.bikini.model.NaverAuth;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RestTemplate restTemplate;

    private final Gson gson;

    @Value("${spring.social.naver.url.login}")
    private String baseUrl;

    @Value("${spring.social.naver.url.profile}")
    private String profileUrl;

    @Value("${spring.social.naver.client_id}")
    private String clientId;

    @Value("${spring.social.naver.client_secret}")
    private String clientSecret;

    @GetMapping("/login/naver")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<Object> login(NaverAuthRequestDto naverAuthRequestDto) {
        String accessToken = naverAuthRequestDto.getAccessToken();
        WebClient webClient = WebClient.builder().build();

        NaverProfileResponseDto response = webClient.get().uri(profileUrl)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                log.error("login error occurs");
                throw new RuntimeException("Naver login exception");
            })
            .bodyToMono(NaverProfileResponseDto.class)
            .block();

        return CommonResponse.empty();
    }

    @GetMapping("/test/login")
    public ModelAndView naverLogin(ModelAndView modelAndView) {
        String redirectUrl = "http://localhost:8080/v1/auth/redirect";

        StringBuilder loginUrl = new StringBuilder(baseUrl);
        loginUrl.append("?client_id=").append(clientId)
            .append("&response_type=code")
            .append("&redirect_uri=").append(redirectUrl);

        modelAndView.addObject("loginUrl", loginUrl.toString());
        modelAndView.setViewName("auth/login");
        return modelAndView;
    }

    @GetMapping("/redirect")
    public ModelAndView redirectNaverLogin(ModelAndView modelAndView, @RequestParam String code) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", "http://localhost:8080/v1/auth/redirect");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity("https://nid.naver.com/oauth2.0/token", request, String.class);

        NaverAuth result = null;

        if (response.getStatusCode() == HttpStatus.OK) {
            result =  gson.fromJson(response.getBody(), NaverAuth.class);
        }

        modelAndView.addObject("authInfo", result);
        modelAndView.setViewName("auth/redirect");
        return modelAndView;
    }
}
