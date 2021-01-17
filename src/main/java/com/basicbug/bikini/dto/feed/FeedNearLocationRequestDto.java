package com.basicbug.bikini.dto.feed;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeedNearLocationRequestDto {

    private double latitude;
    private double longitude;
    private double radius;
}
