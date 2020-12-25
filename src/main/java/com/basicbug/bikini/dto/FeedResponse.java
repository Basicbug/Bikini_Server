package com.basicbug.bikini.dto;

import com.basicbug.bikini.model.Feed;
import com.basicbug.bikini.model.Point;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedResponseDto {

    private Integer feedNumOfUser;
    private String userId;
    private String content;
    private String imageUrl;
    private String profileImageUrl;
    private Integer countOfGroupFeed;
    private Point position;

    public Feed toEntity() {
        return Feed.builder()
            .feedNumOfUser(feedNumOfUser)
            .userId(userId)
            .content(content)
            .imageUrl(imageUrl)
            .profileImageUrl(profileImageUrl)
            .countOfGroupFeed(countOfGroupFeed)
            .position(position)
            .build();
    }
}
