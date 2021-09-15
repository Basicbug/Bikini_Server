package com.basicbug.bikini.feed.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.basicbug.bikini.auth.constant.AuthConstants;
import com.basicbug.bikini.auth.model.AuthProvider;
import com.basicbug.bikini.feed.dto.FeedListResponse;
import com.basicbug.bikini.feed.dto.FeedResponse;
import com.basicbug.bikini.feed.model.Feed;
import com.basicbug.bikini.feed.model.Location;
import com.basicbug.bikini.feed.model.Point;
import com.basicbug.bikini.feed.repository.FeedRepository;
import com.basicbug.bikini.user.model.User;
import com.basicbug.bikini.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("local")
class FeedServiceIntegrationTest {

    @Autowired
    FeedService feedService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FeedRepository feedRepository;

    @Test
    @DisplayName("유저 피드 리스트 통합 테스트")
    @Transactional
    void getFeedListOf() {
        // given
        String username = "ordi";
        User user = User.builder()
                .provider(AuthProvider.NAVER)
                .uid("naver-12345")
                .username(username)
                .build();
        Feed feed = Feed.builder()
                .content("test-content")
                .user(user)
                .location(new Location(new Point(10, 20)))
                .build();
        userRepository.save(user);
        feedRepository.save(feed);

        // when
        List<FeedResponse> feeds = feedService.getFeedListOf(username).getFeeds();

        // then
        assertThat(feeds.size()).isGreaterThan(0);

    }
}