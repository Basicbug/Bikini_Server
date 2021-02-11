package com.basicbug.bikini.service;

import com.basicbug.bikini.dto.auth.NaverAuthRequestDto;
import com.basicbug.bikini.dto.auth.NaverProfileResponseDto;
import com.basicbug.bikini.model.AuthConstants;
import com.basicbug.bikini.model.CommonConstants;
import com.basicbug.bikini.model.NaverProfile;
import com.basicbug.bikini.model.User;
import com.basicbug.bikini.model.UserPrincipal;
import com.basicbug.bikini.repository.UserRepository;
import com.basicbug.bikini.util.JwtTokenProvider;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${spring.social.naver.url.login}")
    private String baseUrl;

    @Value("${spring.social.naver.url.profile}")
    private String profileUrl;

    @Value("${spring.social.naver.client_id}")
    private String clientId;

    @Value("${spring.social.naver.client_secret}")
    private String clientSecret;

    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final WebClient webClient = WebClient.builder().build();

    // TODO: Refactor to more generic way
    public String login(NaverAuthRequestDto naverAuthRequestDto) {
        String accessToken = naverAuthRequestDto.getAccessToken();

        if (accessToken.isEmpty()) return "";

        NaverProfileResponseDto response = webClient.get().uri(profileUrl)
            .header(HttpHeaders.AUTHORIZATION, getAuthorization(accessToken))
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                throw new RuntimeException("naver login exception");
            })
            .bodyToMono(NaverProfileResponseDto.class)
            .block();

        if (response == null) return "";

        NaverProfile profile = response.getResponse();

        final Optional<User> userOptional = userRepository.findUserByEmail(profile.getEmail());

        User user = userOptional.orElseGet(() ->
            new User(profile.getEmail(), new RandomString(10).nextString(),AuthConstants.NORMAL_USER));

        if (!userOptional.isPresent()) {
            userRepository.save(user);
        }

        final UserPrincipal userPrincipal = new UserPrincipal(user);
        return jwtTokenProvider.generateToken(userPrincipal);
    }

    private String getAuthorization(String accessToken) {
        return CommonConstants.TOKEN_PREFIX + accessToken;
    }
}
