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
@NoArgsConstructor
@AllArgsConstructor
public class FeedResponse {

    private UUID feedId;
    private Integer feedNumOfUser;
    private String username;
    private String content;
    private List<String> imageUrl;
    private Integer countOfGroupFeed;
    private Long numOfLikes;
    @JsonProperty("locationInfo")
    private Location location;

    public Feed toEntity() {
        return Feed.builder()
            .feedNumOfUser(feedNumOfUser)
            .content(content)
            .countOfGroupFeed(countOfGroupFeed)
            .location(location)
            .build();
    }
}
