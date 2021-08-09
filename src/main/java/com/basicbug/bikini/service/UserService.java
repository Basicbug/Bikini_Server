package com.basicbug.bikini.service;

import com.basicbug.bikini.dto.auth.AuthRequestDto;
import com.basicbug.bikini.dto.user.UserUpdateRequestDto;
import com.basicbug.bikini.model.KakaoProfile;
import com.basicbug.bikini.model.User;
import com.basicbug.bikini.model.auth.AuthProvider;
import com.basicbug.bikini.model.auth.NaverProfile;
import com.basicbug.bikini.model.auth.exception.InvalidAccessTokenException;
import com.basicbug.bikini.model.auth.exception.UidAlreadyExistException;
import com.basicbug.bikini.model.auth.exception.UserNotFoundException;
import com.basicbug.bikini.repository.UserRepository;
import com.basicbug.bikini.util.JwtTokenProvider;
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
    public void updateUserInfo(String uid, UserUpdateRequestDto userUpdateRequestDto) {
        User newUser = new User();
        newUser.setUid(userUpdateRequestDto.getUsername());

        if (isExistingUid(newUser.getUid())) {
            throw new UidAlreadyExistException("uid already exists " + newUser.getUid());
        }

        User user = userRepository.findByUid(uid).orElseThrow(() -> new UserNotFoundException("user not found with uid " + uid));
        user.updateUserInfo(newUser);
        userRepository.save(user);
    }

    private boolean isExistingUid(String uid) {
        return userRepository.findByUid(uid).isPresent();
    }

    // TODO: Refactor to more generic way
    public String checkOrRegisterUser(AuthRequestDto authRequestDto, AuthProvider provider) {
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
                .provider(provider)
                .roles(Collections.singletonList("ROLE_USER"))
                .build()
        );
    }

    private String getJwtToken(User user) {
        return jwtTokenProvider.generateToken(String.valueOf(user.getId()), user.getRoles());
    }
}
