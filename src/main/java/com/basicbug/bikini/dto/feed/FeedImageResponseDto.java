package com.basicbug.bikini.dto.feed;

import com.basicbug.bikini.model.FeedImage;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class FeedImageResponseDto {

    private Long id;

    public static FeedImageResponseDto of(FeedImage feedImage) {
        return new FeedImageResponseDto(feedImage.getId());
    }

    public static List<FeedImageResponseDto> listOf(List<FeedImage> feedImages) {
        return feedImages.stream()
            .map(FeedImageResponseDto::of)
            .collect(Collectors.toList());
    }
}
