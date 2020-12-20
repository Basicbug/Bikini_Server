package com.basicbug.bikini.dto;

import com.basicbug.bikini.entity.Feed;
import com.basicbug.bikini.model.LatLng;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedRequestDto {

    private Integer feedNumOfUser;
    private String userId;
    private String content;
    private String imageUrl;
    private String profileImageUrl;
    private Integer countOfGroupFeed;
    private LatLng position;

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

    @Override
    public String toString() {
        return "FeedRequestDto(feedNumOfUser=" + feedNumOfUser + ", userId=" + userId + ", content="
            + content + ", imageUrl=" + imageUrl + ", profileImageUrl=" + profileImageUrl
            + ", countOfGroupFeed=" + countOfGroupFeed + ", position=" + position + ")";
    }
}
