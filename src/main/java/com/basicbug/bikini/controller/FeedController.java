package com.basicbug.bikini.controller;


import com.basicbug.bikini.dto.CommonResponse;
import com.basicbug.bikini.dto.FeedRequestDto;
import com.basicbug.bikini.dto.FeedResponseDto;
import com.basicbug.bikini.entity.Feed;
import com.basicbug.bikini.model.LatLng;
import com.basicbug.bikini.repository.FeedRepository;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/feed")
public class FeedController {

    private final Logger logger = LoggerFactory.getLogger(FeedController.class);
    private final FeedRepository feedRepository;

    @GetMapping("/dummy")
    public CommonResponse<FeedResponseDto> getDummyFeedList() {
        FeedResponseDto feedResponseDto = new FeedResponseDto(1, "userId", "content", "imageUrl",
            "profileImageUrl", 3, new LatLng(30.0, 40.0));
        CommonResponse<FeedResponseDto> response = new CommonResponse<>();
        response.setStatus(HttpStatus.OK);
        response.setResult(feedResponseDto);
        return response;
    }

    @ApiOperation(value = "Add feed", notes = "Feed 정보 추가")
    @PostMapping("/add")
    public CommonResponse<Void> addFeed(@RequestBody FeedRequestDto feedRequestDto) {
        logger.debug("addFeed() {}", feedRequestDto);
        Feed feed = feedRequestDto.toEntity();

        feedRepository.save(feed);
        logger.error("add feed {}", feed.getId());

        CommonResponse<Void> response = new CommonResponse<>();
        response.setStatus(HttpStatus.OK);

        return response;
    }

    @ApiOperation(value = "Clear feed list", notes = "Feed 리스트 정보 초기화")
    @GetMapping("/clear")
    public CommonResponse<Void> clearFeedList() {
        logger.debug("clearFeedList()");
        feedRepository.deleteAll();

        CommonResponse<Void> response = new CommonResponse<>();
        response.setStatus(HttpStatus.OK);
        return response;
    }

    @ApiOperation(value = "Get all feed list", notes = "전체 Feed 리스트")
    @GetMapping("/list")
    public CommonResponse<List<FeedResponseDto>> getFeedList() {
        logger.debug("list()");
        List<FeedResponseDto> feedList = feedRepository.findAll().stream().map(Feed::toResponseDto).collect(Collectors.toList());
        CommonResponse<List<FeedResponseDto>> response = new CommonResponse<>();
        response.setResult(feedList);
        response.setStatus(HttpStatus.OK);
        return response;
    }

    @ApiOperation(value = "Get all feed list of userId", notes = "특정 유저의 Feed list")
    @GetMapping("/list/{userId}")
    public CommonResponse<List<FeedResponseDto>> getFeedListOfUser(@RequestParam String userId) {
        logger.debug("get feed list of {}", userId);
        List<FeedResponseDto> feedList = feedRepository.findAll().stream()
            .filter(it -> it.getUserId().equals(userId))
            .map(Feed::toResponseDto)
            .collect(Collectors.toList());

        CommonResponse<List<FeedResponseDto>> response = new CommonResponse<>();
        response.setResult(feedList);
        response.setStatus(HttpStatus.OK);
        return response;
    }
}
