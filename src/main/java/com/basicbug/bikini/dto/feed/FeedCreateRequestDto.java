package com.basicbug.bikini.dto.feed;

import com.basicbug.bikini.model.Feed;
import com.basicbug.bikini.model.Location;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.UUID;
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
public class FeedCreateRequestDto {

    private Integer feedNumOfUser;
    private String userId;
    private String content;
    private String imageUrl;
    private List<Long> imageIds;
    private String profileImageUrl;
    private Integer countOfGroupFeed;
    @JsonProperty("locationInfo")
    private Location location;

    public Feed toEntity() {
        return Feed.builder()
            .feedId(UUID.randomUUID())
            .feedNumOfUser(feedNumOfUser)
            .userId(userId)
            .content(content)
            .imageUrl(imageUrl)
            .profileImageUrl(profileImageUrl)
            .countOfGroupFeed(countOfGroupFeed)
            .location(location)
            .build();
    }

    @Override
    public String toString() {
        return "FeedRequestDto(feedNumOfUser=" + feedNumOfUser + ", userId=" + userId + ", content="
            + content + ", imageUrl=" + imageUrl + ", profileImageUrl=" + profileImageUrl
            + ", countOfGroupFeed=" + countOfGroupFeed + ", location=" + location + ")";
    }
}
