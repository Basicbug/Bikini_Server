package com.basicbug.bikini.entity;

import com.basicbug.bikini.dto.FeedRequestDto;
import com.basicbug.bikini.dto.FeedResponseDto;
import com.basicbug.bikini.model.LatLng;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int feedNumOfUser;
    private int countOfGroupFeed;
    private String userId;
    private String content;
    private String imageUrl;
    private String profileImageUrl;

    @Embedded
    private LatLng position;

    public FeedResponseDto toResponseDto() {
        return FeedResponseDto.builder()
            .feedNumOfUser(feedNumOfUser)
            .userId(userId)
            .content(content)
            .imageUrl(imageUrl)
            .profileImageUrl(profileImageUrl)
            .countOfGroupFeed(countOfGroupFeed)
            .position(position)
            .build();
    }

    public FeedRequestDto toRequestDto() {
        return FeedRequestDto.builder()
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
