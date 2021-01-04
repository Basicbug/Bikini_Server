package com.basicbug.bikini.dto;

import com.basicbug.bikini.model.Feed;
import com.basicbug.bikini.model.Point;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedResponse {

    private UUID feedId;
    private Integer feedNumOfUser;
    private String userId;
    private String content;
    private String imageUrl;
    private String profileImageUrl;
    private Integer countOfGroupFeed;
    private Long numOfLikes;
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
