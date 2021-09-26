package com.basicbug.bikini.user.service;

import com.basicbug.bikini.auth.dto.AuthRequestDto;
import com.basicbug.bikini.auth.model.OAuthToken;
import com.basicbug.bikini.auth.service.KakaoService;
import com.basicbug.bikini.auth.service.NaverService;
import com.basicbug.bikini.user.dto.UserUpdateRequestDto;
import com.basicbug.bikini.auth.model.KakaoProfile;
import com.basicbug.bikini.user.model.User;
import com.basicbug.bikini.auth.model.AuthProvider;
import com.basicbug.bikini.auth.model.NaverProfile;
import com.basicbug.bikini.auth.exception.InvalidAccessTokenException;
import com.basicbug.bikini.auth.exception.UserNotFoundException;
import com.basicbug.bikini.auth.exception.UsernameAlreadyExistException;
import com.basicbug.bikini.user.repository.UserRepository;
import com.basicbug.bikini.auth.util.JwtTokenProvider;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final KakaoService kakaoService;

    private final NaverService naverService;

    public User getUserInformation(String uid) {
        return userRepository.findByUid(uid)
            .orElseThrow(() -> new UserNotFoundException("user not found with uid " + uid));
    }

    @Transactional
    public User updateUserInfo(String uid, UserUpdateRequestDto userUpdateRequestDto) {
        String requestedUserName = userUpdateRequestDto.getUsername();

        if (isExistingUsername(requestedUserName)) {
            throw new UsernameAlreadyExistException("username already exists " + requestedUserName);
        }

        User user = userRepository.findByUid(uid).orElseThrow(() -> new UserNotFoundException("user not found with uid " + uid));
        user.setUsername(userUpdateRequestDto.getUsername());
        userRepository.save(user);

        return user;
    }

    private boolean isExistingUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    // TODO: Refactor to more generic way
    public OAuthToken checkOrRegisterUser(AuthRequestDto authRequestDto, AuthProvider provider) {
        String accessToken = authRequestDto.getAccessToken();

        if (accessToken.isEmpty()) throw new InvalidAccessTokenException("access token should not be empty");

        if (provider == AuthProvider.KAKAO) {
            KakaoProfile profile = kakaoService.getProfile(accessToken);
            Optional<User> userOptional = userRepository.findByUid(profile.getId());
            User user = userOptional.orElseGet(() ->
                registerUserAndGet(profile.getId(), AuthProvider.KAKAO)
            );
            return getJwtToken(user);
        } else if (provider == AuthProvider.NAVER) {
            NaverProfile profile = naverService.getProfile(accessToken);
            Optional<User> userOptional = userRepository.findByUid(profile.getId());
            User user = userOptional.orElseGet(() ->
                registerUserAndGet(profile.getId(), AuthProvider.NAVER)
            );
            return getJwtToken(user);
        }

        throw new InvalidAccessTokenException("fail to get user profile");
    }

    private User registerUserAndGet(String uid, AuthProvider provider) {
        return userRepository.save(
            User.builder()
                .uid(uid)
                .username(uid)
                .provider(provider)
                .roles(Collections.singletonList("ROLE_USER"))
                .build()
        );
    }

    private OAuthToken getJwtToken(User user) {
        final String accessToken = jwtTokenProvider.createAccessToken(user.getUid(), user.getRoles());
        final String refreshToken = jwtTokenProvider.createRefreshToken(user.getUid(), user.getRoles());

        return OAuthToken.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
    }
}
