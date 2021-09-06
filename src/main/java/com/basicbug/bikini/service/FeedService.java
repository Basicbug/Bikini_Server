package com.basicbug.bikini.service;

import com.basicbug.bikini.dto.feed.FeedCreateRequestDto;
import com.basicbug.bikini.dto.feed.FeedDeleteRequestDto;
import com.basicbug.bikini.dto.feed.FeedImageResponseDto;
import com.basicbug.bikini.dto.feed.FeedListResponse;
import com.basicbug.bikini.dto.feed.FeedNearLocationRequestDto;
import com.basicbug.bikini.dto.feed.FeedResponse;
import com.basicbug.bikini.dto.feed.FeedUpdateRequestDto;
import com.basicbug.bikini.model.Feed;
import com.basicbug.bikini.model.FeedImage;
import com.basicbug.bikini.model.Point;
import com.basicbug.bikini.model.User;
import com.basicbug.bikini.model.auth.exception.UserNotFoundException;
import com.basicbug.bikini.model.likes.TargetType;
import com.basicbug.bikini.repository.FeedRepository;
import com.basicbug.bikini.repository.UserRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final LikesService likesService;
    private final ImageService imageService;

    /**
     * DB에 존재하는 모든 피드 목록을 반환한다.
     *
     * @return 전체 피드 목록
     */
    public FeedListResponse getAllFeedResponseList() {
        return new FeedListResponse(feedRepository.findAll()
            .stream()
            .map(this::convertToResponseDto)
            .collect(Collectors.toList()));
    }

    /**
     * userId 를 가진 사용자가 작성한 모든 피드 목록을 반환한다.
     *
     * @param userId 사용자 ID
     * @return 사용자의 피드 목록
     */
    public FeedListResponse getFeedListOf(String userId) {
        // TODO:  Filter 를 쿼리 단에서 하는 게 좋은가 아니면 데이터를 꺼낸 뒤 수행하는 것이 좋은가?
        return new FeedListResponse(feedRepository.findAll()
            .stream()
            .filter(it -> it.getUser().getUid().equals(userId))
            .map(this::convertToResponseDto)
            .collect(Collectors.toList()));
    }

    /**
     * Likes 로 정렬 시 상위 limit 개의 피드를 반환한다.
     *
     * @param limit 반환할 피드의 수
     * @return Likes 로 정렬한 피드 중 상위 limit 개의 리스트
     */
    public FeedListResponse getMostLikesFeedList(int limit) {
        List<Feed> allFeeds = feedRepository.findAll();
        List<String> topFeedIds = likesService.getMostLikesTargetIds(TargetType.FEED, limit);

        allFeeds.sort(new Comparator<Feed>() {
            @Override
            public int compare(Feed o1, Feed o2) {
                if (o1.getModifiedAt().isBefore(o2.getModifiedAt())) {
                    return -1;
                } else if (o1.getModifiedAt().isAfter(o2.getModifiedAt())) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        Set<Feed> selectedFeeds = allFeeds
            .stream()
            .filter(it -> topFeedIds.contains(it.getFeedId().toString()))
            .collect(Collectors.toSet());

        for (Feed feed : allFeeds) {
            if (selectedFeeds.contains(feed)) continue;
            selectedFeeds.add(feed);
            if (selectedFeeds.size() >= limit) break;
        }

        return new FeedListResponse(selectedFeeds
            .stream()
            .map(this::convertToResponseDto)
            .collect(Collectors.toList())
        );
    }

    /**
     * Get feed lists that posted near by specified location
     *
     * @param feedNearLocationRequestDto
     * @return feed list that posted near by specified location
     */
    public FeedListResponse getNearByFeedList(FeedNearLocationRequestDto feedNearLocationRequestDto) {
        Point point = new Point(feedNearLocationRequestDto.getLatitude(), feedNearLocationRequestDto.getLongitude());
        double radius = feedNearLocationRequestDto.getRadius();
        List<Feed> feeds = feedRepository.findFeedsNearLocation(point.getLongitude(), point.getLatitude(), radius);
        return new FeedListResponse(
            feeds.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList())
        );
    }

    /**
     * Save Feed information to database
     *
     * @param feedCreateRequestDto FeedDto that has feed information to be saved
     * @param uid uid of user that posts this feed
     */
    @Transactional
    public void createFeed(FeedCreateRequestDto feedCreateRequestDto, String uid) {
        final List<FeedImage> feedImages = imageService.findAllFeedImageByIds(feedCreateRequestDto.getImageIds());
        final User user = userRepository.findByUid(uid).orElseThrow(() -> new UserNotFoundException("user not found with uid " + uid));
        final Feed feed = createFeedByRequest(feedCreateRequestDto, feedImages, user);
        feedImages.forEach(feedImage -> feedImage.setFeed(feed));
        feedRepository.save(feed);
    }

    /**
     * Update Feed information
     *
     * @param feedUpdateRequestDto
     */
    @Transactional
    public void updateFeed(FeedUpdateRequestDto feedUpdateRequestDto) {
        Feed newFeed = feedUpdateRequestDto.toEntity();
        Feed oldFeed = feedRepository.findByFeedId(newFeed.getFeedId());
        oldFeed.update(newFeed);

        feedRepository.save(oldFeed);
    }

    /**
     * Remove Feed associated with feedId inside of feedDeleteRequestDto
     *
     * @param feedDeleteRequestDto
     * @return True if delete success otherwise False
     */
    @Transactional
    public boolean deleteFeed(FeedDeleteRequestDto feedDeleteRequestDto) {
        // TODO: Need to validate feedId
        long result = feedRepository.deleteByFeedId(UUID.fromString(feedDeleteRequestDto.getFeedId()));
        return result != 0;
    }

    /**
     * 현재 DB에 존재하는 모든 피드 목록을 삭제한다.
     */
    @Transactional
    public void clearFeedList() {
        feedRepository.deleteAll();
    }

    private Feed createFeedByRequest(FeedCreateRequestDto createRequestDto, List<FeedImage> feedImages, User user) {
        return Feed.builder()
            .feedId(UUID.randomUUID())
            .feedNumOfUser(createRequestDto.getFeedNumOfUser())
            .user(user)
            .content(createRequestDto.getContent())
            .images(feedImages)
            .imageUrl(createRequestDto.getImageUrl())
            .location(createRequestDto.getLocation())
            .build();
    }

    public List<FeedImageResponseDto> uploadImages(List<MultipartFile> images, String dirName) {
        List<FeedImage> feedImages = imageService.uploadImages(images, dirName);
        return FeedImageResponseDto.listOf(feedImages);
    }

    private FeedResponse convertToResponseDto(Feed feed) {
        int numOfLikes = likesService.getLikesCount(TargetType.FEED, feed.getFeedId().toString());
        FeedResponse response = feed.toResponseDto();
        response.setNumOfLikes(numOfLikes);
        return response;
    }
}
