package com.basicbug.bikini.user.service;

import com.basicbug.bikini.auth.exception.UserNotFoundException;
import com.basicbug.bikini.feed.repository.FeedRepository;
import com.basicbug.bikini.likes.repository.LikesRepository;
import com.basicbug.bikini.user.dto.UserV2Info;
import com.basicbug.bikini.user.model.User;
import com.basicbug.bikini.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserV2Service {

    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final LikesRepository likesRepository;

    public UserV2Info getUserInfo(String uid) {
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new UserNotFoundException("user not found with uid " + uid));

        return UserV2Info.builder()
                .username(user.getNickname())
                .feedCount(feedRepository.countByUser(user))
                .likeCount(likesRepository.countByUser(user))
                .build();
    }

    public void updateUserInfo(String uid, User newUser) {
        User user = userRepository.findByUid(uid)
            .orElseThrow(() -> new UserNotFoundException("user not found with uid " + uid));

        user.setUsername(newUser.getUsername());
        userRepository.save(user);
    }
}
