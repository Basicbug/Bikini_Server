package com.basicbug.bikini.controller;


import com.basicbug.bikini.entity.CommonResponse;
import com.basicbug.bikini.entity.FeedResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/feed")
public class FeedController {

    private final Logger logger = LoggerFactory.getLogger(FeedController.class);

    @GetMapping("/dummy")
    public CommonResponse<FeedResponseDto> getDummyFeedList() {
        FeedResponseDto feedResponseDto = new FeedResponseDto(1, "userId", "content", "imageUrl", "profileImageUrl", 3);
        CommonResponse<FeedResponseDto> response = new CommonResponse<>();
        response.setContent(feedResponseDto);
        return response;
    }
}
