package com.basicbug.bikini.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedDeleteRequestDto {
    private String feedId;

    public FeedDeleteRequestDto(final String feedId) {
        this.feedId = feedId;
    }
}
