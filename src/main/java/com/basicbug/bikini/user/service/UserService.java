package com.basicbug.bikini.user.service;

import com.basicbug.bikini.auth.dto.AuthRequestDto;
import com.basicbug.bikini.auth.dto.JwtTokenRefreshRequestDto;
import com.basicbug.bikini.auth.exception.InvalidAccessTokenException;
import com.basicbug.bikini.auth.exception.UserNotFoundException;
import com.basicbug.bikini.auth.exception.UsernameAlreadyExistException;
import com.basicbug.bikini.auth.model.AuthProvider;
import com.basicbug.bikini.auth.model.KakaoProfile;
import com.basicbug.bikini.auth.model.NaverProfile;
import com.basicbug.bikini.auth.model.OAuthToken;
import com.basicbug.bikini.auth.model.RefreshToken;
import com.basicbug.bikini.auth.repository.RefreshTokenRepository;
import com.basicbug.bikini.auth.service.KakaoService;
import com.basicbug.bikini.auth.service.NaverService;
import com.basicbug.bikini.auth.util.JwtTokenProvider;
import com.basicbug.bikini.user.dto.UserUpdateRequestDto;
import com.basicbug.bikini.user.exception.InvalidRefreshTokenException;
import com.basicbug.bikini.user.exception.RefreshTokenNotFoundException;
import com.basicbug.bikini.user.exception.RefreshTokenNotMatchedException;
import com.basicbug.bikini.user.model.User;
import com.basicbug.bikini.user.repository.UserRepository;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

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

    public OAuthToken refreshToken(JwtTokenRefreshRequestDto requestDto) {
        String accessToken = requestDto.getAccessToken();
        String refreshToken = requestDto.getRefreshToken();

        if (!jwtTokenProvider.isValidToken(refreshToken)) {
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        RefreshToken savedRefreshToken = refreshTokenRepository.findByTokenId(authentication.getName())
            .orElseThrow(() -> new RefreshTokenNotFoundException("token is not exists"));

        if (!savedRefreshToken.getToken().equals(refreshToken)) {
            throw new RefreshTokenNotMatchedException("refresh token is not matched");
        }

        User user = userRepository.findByUid(authentication.getName())
            .orElseThrow(() -> new UserNotFoundException("user is not exists"));
        OAuthToken updatedToken = getJwtToken(user);

        RefreshToken updatedRefreshToken = savedRefreshToken.updateToken(updatedToken.getRefreshToken());
        refreshTokenRepository.save(updatedRefreshToken);

        return updatedToken;
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
        OAuthToken oAuthToken = OAuthToken.builder()
            .accessToken(jwtTokenProvider.createAccessToken(user.getUid(), user.getRoles()))
            .refreshToken(jwtTokenProvider.createRefreshToken(user.getUid(), user.getRoles()))
            .build();

        RefreshToken refreshToken = RefreshToken.builder().tokenId(user.getUid())
            .token(oAuthToken.getRefreshToken())
            .expireTime(jwtTokenProvider.getExpireTime(oAuthToken.getRefreshToken()))
            .build();

        refreshTokenRepository.save(refreshToken);

        return oAuthToken;
    }
}
