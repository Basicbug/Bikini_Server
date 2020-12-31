package com.basicbug.bikini.controller;


import com.basicbug.bikini.dto.CommonResponse;
import com.basicbug.bikini.dto.FeedListResponse;
import com.basicbug.bikini.dto.FeedRequestDto;
import com.basicbug.bikini.model.Feed;
import com.basicbug.bikini.service.FeedService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/feed")
public class FeedController {

    private final FeedService feedService;

    @ApiOperation(value = "Add feed", notes = "Feed 정보 추가")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponse<Void> addFeed(@RequestBody FeedRequestDto feedRequestDto) {
        //TODO model <-> dto 변환은 어디서 수행하는 것이 좋을까?
        Feed feed = feedRequestDto.toEntity();
        feedService.createFeed(feed);
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

    @ApiOperation(value = "Get all feed list of userId", notes = "특정 유저의 Feed list")
    @GetMapping("/list/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<FeedListResponse> getFeedListOfUser(@PathVariable String userId) {
        FeedListResponse feedListResponse = feedService.getFeedListOf(userId);
        return CommonResponse.of(feedListResponse);
    }
}
