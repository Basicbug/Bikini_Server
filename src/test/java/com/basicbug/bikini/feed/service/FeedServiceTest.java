package com.basicbug.bikini.feed.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.basicbug.bikini.feed.dto.FeedDeleteRequestDto;
import com.basicbug.bikini.feed.dto.FeedUpdateRequestDto;
import com.basicbug.bikini.feed.model.Feed;
import com.basicbug.bikini.feed.model.Location;
import com.basicbug.bikini.feed.repository.FeedRepository;
import java.util.UUID;

import com.basicbug.bikini.feed.service.FeedService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FeedServiceTest {

    @InjectMocks
    FeedService feedService;

    @Mock
    FeedRepository feedRepository;

    private final UUID TEST_UUID = UUID.fromString("ad79ceff-5473-4b21-8b7e-2d15fdb7dee8");
    private final int TEST_FEED_NUM_OF_USER = 0;
    private final String TEST_USER_ID = "TEST_USER";
    private final String TEST_CONTENT = "TEST_CONTENT";
    private final String TEST_IMAGE_URL = "TEST_IMAGE_URL";
    private final int TEST_COUNT_OF_GROUP_FEED = 0;
    private final Location TEST_LOCATION = new Location();
    private final Feed TEST_FEED = Feed.builder().feedId(TEST_UUID).build();

    @Test
    void delete_feed_when_exists() {
        // given
        feedRepository.save(TEST_FEED);

        // when
        FeedDeleteRequestDto requestDto = new FeedDeleteRequestDto(TEST_UUID.toString());
        feedService.deleteFeed(requestDto);

        // then
        verify(feedRepository).deleteByFeedId(TEST_UUID);
    }

    @Test
    void update_feed_when_exists() {
        // given
        feedRepository.save(TEST_FEED);
        when(feedRepository.findByFeedId(TEST_UUID)).thenReturn(TEST_FEED);

        // when
        String newContent = "updated_content";
        FeedUpdateRequestDto requestDto = FeedUpdateRequestDto.builder()
            .feedId(TEST_UUID.toString())
            .feedNumOfUser(TEST_FEED_NUM_OF_USER)
            .userId(TEST_USER_ID)
            .content(newContent)
            .imageUrl(TEST_IMAGE_URL)
            .location(TEST_LOCATION)
            .build();

        feedService.updateFeed(requestDto);

        // then
        verify(feedRepository).findByFeedId(TEST_UUID);
        verify(feedRepository, times(2)).save(any());
        assertEquals(newContent, feedRepository.findByFeedId(TEST_UUID).getContent());
    }

    @AfterEach
    void cleanUp() {
        feedRepository.deleteAll();
    }

}
