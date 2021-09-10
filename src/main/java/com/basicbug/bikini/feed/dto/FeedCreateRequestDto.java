package com.basicbug.bikini.feed.dto;

import com.basicbug.bikini.feed.model.Feed;
import com.basicbug.bikini.feed.model.Location;
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
    private String content;
    private String imageUrl;
    private List<Long> imageIds;
    @JsonProperty("locationInfo")
    private Location location;

    public Feed toEntity() {
        return Feed.builder()
            .feedId(UUID.randomUUID())
            .feedNumOfUser(feedNumOfUser)
            .content(content)
            .imageUrl(imageUrl)
            .location(location)
            .build();
    }

    @Override
    public String toString() {
        return "FeedRequestDto(feedNumOfUser=" + feedNumOfUser + ", content="
            + content + ", imageUrl=" + imageUrl + ", location=" + location + ")";
    }
}
