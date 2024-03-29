package com.basicbug.bikini.feed.controller;


import static com.basicbug.bikini.auth.constant.AuthConstants.NORMAL_USER;
import static com.basicbug.bikini.common.type.CommonResponseCode.FAIL_TO_PROCESS;
import static com.basicbug.bikini.common.type.CommonResponseCode.SUCCESS;

import com.basicbug.bikini.common.dto.CommonResponse;
import com.basicbug.bikini.feed.dto.FeedCreateRequestDto;
import com.basicbug.bikini.feed.dto.FeedDeleteRequestDto;
import com.basicbug.bikini.feed.dto.FeedImageResponseDto;
import com.basicbug.bikini.feed.dto.FeedListResponse;
import com.basicbug.bikini.feed.dto.FeedNearLocationRequestDto;
import com.basicbug.bikini.feed.dto.FeedUpdateRequestDto;
import com.basicbug.bikini.feed.service.FeedService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "JWT token", required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "Add feed", notes = "Feed 정보 추가")
    @Secured(NORMAL_USER)
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponse<Void> addFeed(@RequestBody FeedCreateRequestDto feedCreateRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();
        log.info("add feed uid : {}", uid);
        feedService.createFeed(feedCreateRequestDto, uid);

        return CommonResponse.of(SUCCESS);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "JWT token", required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "Delete Feed", notes = "Feed 정보 삭제")
    @Secured(NORMAL_USER)
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CommonResponse<Void> deleteFeed(@RequestBody FeedDeleteRequestDto feedDeleteRequestDto) {
        boolean result = feedService.deleteFeed(feedDeleteRequestDto);

        if (result) {
            return CommonResponse.of(SUCCESS);
        } else {
            return CommonResponse.error(FAIL_TO_PROCESS);
        }
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "JWT token", required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "Update feed", notes = "Feed 정보 갱신")
    @Secured(NORMAL_USER)
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<Void> updateFeed(@RequestBody FeedUpdateRequestDto feedUpdateRequestDto) {
        feedService.updateFeed(feedUpdateRequestDto);
        return CommonResponse.of(SUCCESS);
    }

    @ApiOperation(value = "Get all feed list", notes = "전체 Feed 리스트")
    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<FeedListResponse> getFeedList() {
        FeedListResponse feedListResponse = feedService.getAllFeedResponseList();
        return CommonResponse.of(feedListResponse, SUCCESS);
    }

    @ApiOperation(value = "Get top most likes feed list", notes = "좋아요 수 상위 limit 개의 피드 리스트")
    @GetMapping("/list/top/{limit}")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<FeedListResponse> getTopFeedList(@PathVariable int limit) {
        FeedListResponse feedListResponse = feedService.getMostLikesFeedList(limit);
        return CommonResponse.of(feedListResponse, SUCCESS);
    }

    @ApiOperation(value = "Get all feed list of userId", notes = "특정 유저의 Feed list")
    @GetMapping("/list/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<FeedListResponse> getFeedListOfUser(@PathVariable String userId) {
        FeedListResponse feedListResponse = feedService.getFeedListOf(userId);
        return CommonResponse.of(feedListResponse, SUCCESS);
    }

    @ApiOperation(value = "Get feed list that posted near by specified location")
    @GetMapping("/nearby")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<FeedListResponse> getNearLocationFeedList(FeedNearLocationRequestDto feedNearLocationRequestDto) {
        FeedListResponse feedListResponse = feedService.getNearByFeedList(feedNearLocationRequestDto);
        return CommonResponse.of(feedListResponse, SUCCESS);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "JWT token", required = true, dataType = "String", paramType = "header")
    @Secured(NORMAL_USER)
    @GetMapping("/list/me")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<FeedListResponse> getMyFeedList() {
        FeedListResponse feedListResponse = feedService.getAllFeedResponseList();
        return CommonResponse.of(feedListResponse, SUCCESS);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "JWT token", required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "Upload images", notes = "피드 내 이미지 업로드")
    @Secured(NORMAL_USER)
    @PostMapping("/upload/images")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponse<List<FeedImageResponseDto>> uploadImage(List<MultipartFile> images) {
        log.info("uploadImage request ${}", images);
        List<FeedImageResponseDto> feedImages = feedService.uploadImages(images, FEED_IMAGE_DIR);
        return CommonResponse.of(feedImages, SUCCESS);
    }
}
