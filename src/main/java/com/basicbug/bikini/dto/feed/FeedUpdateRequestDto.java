package com.basicbug.bikini.dto.feed;

import com.basicbug.bikini.model.Feed;
import com.basicbug.bikini.model.Location;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FeedUpdateRequestDto {

    private String feedId;
    private Integer feedNumOfUser;
    private String userId;
    private String content;
    private String imageUrl;
    private String profileImageUrl;
    private Integer countOfGroupFeed;
    @JsonProperty("locationInfo")
    private Location location;

    public Feed toEntity() {
        return Feed.builder()
            .feedId(UUID.fromString(feedId))
            .feedNumOfUser(feedNumOfUser)
            .content(content)
            .imageUrl(imageUrl)
            .countOfGroupFeed(countOfGroupFeed)
            .location(location)
            .build();
    }
}
