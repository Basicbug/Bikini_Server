package com.basicbug.bikini.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FeedResponseDto {
    private Integer feedNumOfUser;
    private String userId;
    private String content;
    private String imageUrl;
    private String profileImageUrl;
    private Integer countOfGroupFeed;
}
