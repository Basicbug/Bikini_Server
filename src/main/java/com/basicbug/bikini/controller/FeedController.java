package com.basicbug.bikini.controller;


import com.basicbug.bikini.dto.CommonResponse;
import com.basicbug.bikini.dto.FeedResponseDto;
import com.basicbug.bikini.entity.Feed;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/feed")
public class FeedController {

    private final Logger logger = LoggerFactory.getLogger(FeedController.class);
    private final Map<String, Feed> DB = new HashMap<>();

    @GetMapping("/dummy")
    public CommonResponse<FeedResponseDto> getDummyFeedList() {
        FeedResponseDto feedResponseDto = new FeedResponseDto(1, "userId", "content", "imageUrl", "profileImageUrl", 3);
        CommonResponse<FeedResponseDto> response = new CommonResponse<>();
        response.setStatus(HttpStatus.OK);
        response.setResult(feedResponseDto);
        return response;
    }

    @PostMapping("/add")
    public CommonResponse<Void> addFeed(
        @RequestParam Integer feedNumOfUser,
        @RequestParam String userId,
        @RequestParam String content,
        @RequestParam String imageUrl,
        @RequestParam String profileImageUrl,
        @RequestParam Integer countOfGroupFeed
    ) {
        Feed feed = new Feed(feedNumOfUser, userId, content, imageUrl, profileImageUrl, countOfGroupFeed);
        DB.put(userId, feed);

        CommonResponse<Void> response = new CommonResponse<>();
        response.setStatus(HttpStatus.OK);

        return response;
    }

    @GetMapping("/list")
    public CommonResponse<List<FeedResponseDto>> getFeedList() {
        List<FeedResponseDto> feedList = DB.values().stream().map(Feed::toDto).collect(Collectors.toList());
        CommonResponse<List<FeedResponseDto>> response = new CommonResponse<>();
        response.setResult(feedList);
        response.setStatus(HttpStatus.OK);
        return response;
    }
}
