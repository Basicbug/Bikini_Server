package com.basicbug.bikini.service;

import com.basicbug.bikini.dto.FeedDeleteRequestDto;
import com.basicbug.bikini.dto.FeedListResponse;
import com.basicbug.bikini.dto.FeedUpdateRequestDto;
import com.basicbug.bikini.model.Feed;
import com.basicbug.bikini.repository.FeedRepository;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;

    /**
     * DB에 존재하는 모든 피드 목록을 반환한다.
     *
     * @return 전체 피드 목록
     */
    public FeedListResponse getAllFeedResponseList() {
        return new FeedListResponse(feedRepository.findAll()
            .stream()
            .map(Feed::toResponseDto)
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
            .filter(it -> it.getUserId().equals(userId))
            .map(Feed::toResponseDto)
            .collect(Collectors.toList()));
    }

    /**
     * numOfLikes 로 정렬 시 상위 limit 개의 피드를 반환한다.
     * @param limit 반환할 피드의 수
     * @return numOfLikes 로 정렬한 피드 중 상위 limit 개의 리스트
     */
    public FeedListResponse getMostLikesFeedList(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("numOfLikes").descending());
        return new FeedListResponse(feedRepository.findAll(pageable)
            .stream()
            .map(Feed::toResponseDto)
            .collect(Collectors.toList()));
    }

    /**
     * 전달받은 피드를 DB에 저장한다.
     *
     * @param feed 등록할 피드
     */
    public void createFeed(Feed feed) {
        //TODO: Feed 생성 성공/실패 여부 처리 필요
        feedRepository.save(feed);
    }

    /**
     * Update Feed information
     * @param feedUpdateRequestDto
     */
    public void updateFeed(FeedUpdateRequestDto feedUpdateRequestDto) {
        Feed newFeed = feedUpdateRequestDto.toEntity();
        Feed oldFeed = feedRepository.findByFeedId(newFeed.getFeedId());
        oldFeed.update(newFeed);

        feedRepository.save(oldFeed);
    }

    /**
     * Remove Feed associated with feedId inside of feedDeleteRequestDto
     * @param feedDeleteRequestDto
     * @return True if delete success otherwise False
     */
    public boolean deleteFeed(FeedDeleteRequestDto feedDeleteRequestDto) {
        // TODO: Need to validate feedId
        long result = feedRepository.deleteByFeedId(UUID.fromString(feedDeleteRequestDto.getFeedId()));
        return result != 0;
    }

    /**
     * 현재 DB에 존재하는 모든 피드 목록을 삭제한다.
     */
    public void clearFeedList() {
        feedRepository.deleteAll();
    }
}
