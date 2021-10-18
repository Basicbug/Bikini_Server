package com.basicbug.bikini.feed.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.basicbug.bikini.auth.constant.AuthConstants;
import com.basicbug.bikini.auth.model.AuthProvider;
import com.basicbug.bikini.feed.model.Feed;
import com.basicbug.bikini.feed.model.Location;
import com.basicbug.bikini.feed.model.Point;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.basicbug.bikini.user.model.User;
import com.basicbug.bikini.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("local")
@Transactional
class FeedRepositoryTest {

    @Autowired
    FeedRepository feedRepository;
    @Autowired
    UserRepository userRepository;

    @AfterEach
    public void cleanup() {
        feedRepository.deleteAll();
    }

    @Test
    public void 피드_전체리스트_불러오기() {
        //given
        UUID uuid = UUID.randomUUID();
        String content = "test_content";
        Location location = new Location(new Point(0.0, 0.0));

        feedRepository.save(Feed.builder().feedId(uuid).content(content).location(location).build());

        //when
        List<Feed> feeds = feedRepository.findAll();

        //then
        Feed feed = feeds.get(0);
        assertThat(feed.getFeedId()).isEqualTo(uuid);
        assertThat(feed.getContent()).isEqualTo(content);
    }

    @Test
    public void countByUser() {
        // given
        User user = getTestUser();
        userRepository.save(user);

        Feed feed1 = getTestFeed(user);
        Feed feed2 = getTestFeed(user);
        feedRepository.save(feed1);
        feedRepository.save(feed2);

        // when
        long count = feedRepository.countByUser(user);

        // then
        assertThat(count).isEqualTo(2L);
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

    private Feed getTestFeed(User user) {
        return Feed.builder()
                .feedId(UUID.randomUUID())
                .content("test_content")
                .location(new Location(new Point(0.0, 0.0)))
                .user(user)
                .build();
    }
}
