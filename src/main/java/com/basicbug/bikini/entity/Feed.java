package com.basicbug.bikini.entity;

import com.basicbug.bikini.dto.FeedResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Feed {
    private Integer feedNumOfUser;
    private String userId;
    private String content;
    private String imageUrl;
    private String profileImageUrl;
    private Integer countOfGroupFeed;

    public FeedResponseDto toDto() {
        return FeedResponseDto.builder()
            .feedNumOfUser(feedNumOfUser)
            .userId(userId)
            .content(content)
            .imageUrl(imageUrl)
            .profileImageUrl(profileImageUrl)
            .countOfGroupFeed(countOfGroupFeed)
            .build();
    }
}
