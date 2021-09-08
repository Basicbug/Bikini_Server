package com.basicbug.bikini.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeedNearLocationRequestDto {

    private double latitude;
    private double longitude;
    private double radius;
}
