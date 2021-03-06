package com.basicbug.bikini.controller;


import com.basicbug.bikini.dto.common.CommonResponse;
import com.basicbug.bikini.dto.feed.FeedCreateRequestDto;
import com.basicbug.bikini.dto.feed.FeedDeleteRequestDto;
import com.basicbug.bikini.dto.feed.FeedImageResponseDto;
import com.basicbug.bikini.dto.feed.FeedListResponse;
import com.basicbug.bikini.dto.feed.FeedNearLocationRequestDto;
import com.basicbug.bikini.dto.feed.FeedUpdateRequestDto;
import com.basicbug.bikini.model.FeedImage;
import com.basicbug.bikini.service.FeedService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/feed")
public class FeedController {

    private final FeedService feedService;
    private static final String FEED_IMAGE_DIR = "feed";

    @ApiOperation(value = "Add feed", notes = "Feed 정보 추가")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponse<Void> addFeed(@RequestBody FeedCreateRequestDto feedCreateRequestDto) {
        feedService.createFeed(feedCreateRequestDto);
        return CommonResponse.empty();
    }

    @ApiOperation(value = "Delete Feed", notes = "Feed 정보 삭제")
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CommonResponse<Void> deleteFeed(@RequestBody FeedDeleteRequestDto feedDeleteRequestDto) {
        boolean result = feedService.deleteFeed(feedDeleteRequestDto);
        return CommonResponse.empty();
    }

    @ApiOperation(value = "Update feed", notes = "Feed 정보 갱신")
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<Void> updateFeed(@RequestBody FeedUpdateRequestDto feedUpdateRequestDto) {
        feedService.updateFeed(feedUpdateRequestDto);
        return CommonResponse.empty();
    }

    @ApiOperation(value = "Clear feed list", notes = "Feed 리스트 정보 초기화")
    @GetMapping("/clear")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CommonResponse<Void> clearFeedList() {
        feedService.clearFeedList();
        return CommonResponse.empty();
    }

    @ApiOperation(value = "Get all feed list", notes = "전체 Feed 리스트")
    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<FeedListResponse> getFeedList() {
        FeedListResponse feedListResponse = feedService.getAllFeedResponseList();
        return CommonResponse.of(feedListResponse);
    }

    @ApiOperation(value = "Get top most likes feed list", notes = "좋아요 수 상위 limit 개의 피드 리스트")
    @GetMapping("/list/top/{limit}")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<FeedListResponse> getTopFeedList(@PathVariable int limit) {
        FeedListResponse feedListResponse = feedService.getMostLikesFeedList(limit);
        return CommonResponse.of(feedListResponse);
    }

    @ApiOperation(value = "Get all feed list of userId", notes = "특정 유저의 Feed list")
    @GetMapping("/list/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<FeedListResponse> getFeedListOfUser(@PathVariable String userId) {
        FeedListResponse feedListResponse = feedService.getFeedListOf(userId);
        return CommonResponse.of(feedListResponse);
    }

    @ApiOperation(value = "Get feed list that posted near by specified location")
    @GetMapping("/nearby")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<FeedListResponse> getNearLocationFeedList(FeedNearLocationRequestDto feedNearLocationRequestDto) {
        FeedListResponse feedListResponse = feedService.getNearByFeedList(feedNearLocationRequestDto);
        return CommonResponse.of(feedListResponse);
    }

    @ApiOperation(value = "Upload images", notes = "피드 내 이미지 업로드")
    @PostMapping("/upload/images")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponse<List<FeedImageResponseDto>> uploadImage(List<MultipartFile> images) {
        log.info("uploadImage request ${}", images);
        List<FeedImageResponseDto> feedImages = feedService.uploadImages(images, FEED_IMAGE_DIR);
        return CommonResponse.of(feedImages);
    }
}
