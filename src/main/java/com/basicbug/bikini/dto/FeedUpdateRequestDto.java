package com.basicbug.bikini.dto;

import com.basicbug.bikini.model.Feed;
import com.basicbug.bikini.model.Point;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedUpdateRequestDto {

    private String feedId;
    private Integer feedNumOfUser;
    private String userId;
    private String content;
    private String imageUrl;
    private String profileImageUrl;
    private Integer countOfGroupFeed;
    private Point position;

    public Feed toEntity() {
        return Feed.builder()
            .feedId(UUID.fromString(feedId))
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
