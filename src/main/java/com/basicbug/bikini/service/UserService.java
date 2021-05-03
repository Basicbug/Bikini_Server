package com.basicbug.bikini.service;

import com.basicbug.bikini.dto.auth.AuthRequestDto;
import com.basicbug.bikini.dto.auth.KakaoProfileResponseDto;
import com.basicbug.bikini.dto.auth.NaverProfileResponseDto;
import com.basicbug.bikini.model.AuthConstants;
import com.basicbug.bikini.model.CommonConstants;
import com.basicbug.bikini.model.User;
import com.basicbug.bikini.model.UserPrincipal;
import com.basicbug.bikini.model.auth.NaverProfile;
import com.basicbug.bikini.model.auth.exception.OAuthProcessException;
import com.basicbug.bikini.repository.UserRepository;
import com.basicbug.bikini.util.JwtTokenProvider;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final WebClient webClient = WebClient.builder().build();

    // TODO: Refactor to more generic way
    public String loadUserInfo(AuthRequestDto authRequestDto, String provider) {
        String accessToken = authRequestDto.getAccessToken();

        if (accessToken.isEmpty()) return "";

        String profileUrl = "";
        if (provider.equals("kakao")) {
            profileUrl = "https://kapi.kakao.com/v2/user/me";
        } else {
            profileUrl = "https://openapi.naver.com/v1/nid/me";
        }

        WebClient.ResponseSpec spec = webClient.get().uri(profileUrl)
            .header(HttpHeaders.AUTHORIZATION, getAuthorization(accessToken))
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                throw new OAuthProcessException("Fail to get profile");
            });

        return requestProfileAndGetJwtToken(provider, spec);
    }

    private String requestProfileAndGetJwtToken(String provider, WebClient.ResponseSpec spec) {
        String email = "";

        try {
            if (provider.equals("kakao")) {
                KakaoProfileResponseDto response = spec.bodyToMono(KakaoProfileResponseDto.class).block();
                if (response != null) {
                    Long id = response.getId();
                    email = id + "@kakao.com";
                }
            } else {
                NaverProfileResponseDto response = spec.bodyToMono(NaverProfileResponseDto.class).block();
                if (response != null) {
                    NaverProfile profile = response.getResponse();
                    email = profile.getId() + "@naver.com";
                }
            }
        } catch (OAuthProcessException ex) {
            email = "";
        }

        if (email.isEmpty()) return "";

        final Optional<User> userOptional = userRepository.findUserByEmail(email);
        final String targetEmail = email;
        final User user = userOptional.orElseGet(() -> new User(targetEmail, new RandomString(10).nextString(), AuthConstants.NORMAL_USER));

        if (!userOptional.isPresent()) {
            userRepository.save(user);
        }

        return jwtTokenProvider.generateToken(new UserPrincipal(user));
    }

    private String getAuthorization(String accessToken) {
        return CommonConstants.TOKEN_PREFIX + accessToken;
    }
}
