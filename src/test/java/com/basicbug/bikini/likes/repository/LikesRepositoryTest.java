package com.basicbug.bikini.likes.repository;

import com.basicbug.bikini.auth.constant.AuthConstants;
import com.basicbug.bikini.auth.model.AuthProvider;
import com.basicbug.bikini.likes.model.Likes;
import com.basicbug.bikini.likes.type.TargetType;
import com.basicbug.bikini.user.model.User;
import com.basicbug.bikini.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("local")
@Transactional
class LikesRepositoryTest {

    @Autowired
    LikesRepository likesRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    void countByUser() {
        // given
        User user = getTestUser();
        userRepository.save(user);

        Likes likes1 = getLikes(user);
        Likes likes2 = getLikes(user);
        likesRepository.save(likes1);
        likesRepository.save(likes2);

        // when
        long count = likesRepository.countByUser(user);

        // then
        assertTrue(count > 0);
    }

    private User getTestUser() {
        return User.builder()
                .uid("123")
                .username("ordi_test")
                .password("123")
                .roles(Collections.singletonList(AuthConstants.NORMAL_USER))
                .provider(AuthProvider.NAVER)
                .build();
    }

    private Likes getLikes(User user) {
        Likes result = new Likes();
        result.setTargetType(TargetType.FEED);
        result.setTargetId(UUID.randomUUID().toString());
        result.setUser(user);
        return result;
    }
}