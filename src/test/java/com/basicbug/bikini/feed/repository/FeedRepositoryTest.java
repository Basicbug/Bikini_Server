package com.basicbug.bikini.feed.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.basicbug.bikini.feed.model.Feed;
import com.basicbug.bikini.feed.model.Location;
import com.basicbug.bikini.feed.model.Point;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("local")
class FeedRepositoryTest {

    @Autowired
    FeedRepository feedRepository;

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
}
