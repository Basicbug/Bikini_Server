package com.basicbug.bikini.model;

import com.basicbug.bikini.dto.FeedCreateRequestDto;
import com.basicbug.bikini.dto.FeedResponse;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(columnDefinition = "VARCHAR(36)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID feedId = UUID.randomUUID();

    private int feedNumOfUser;
    private int countOfGroupFeed;
    private long numOfLikes;
    private String userId;
    private String content;
    private String imageUrl;
    private String profileImageUrl;

    @Embedded
    private Point position;

    public void update(Feed newFeed) {
        this.feedNumOfUser = newFeed.feedNumOfUser;
        this.countOfGroupFeed = newFeed.countOfGroupFeed;
        this.numOfLikes = newFeed.numOfLikes;
        this.userId = newFeed.userId;
        this.content = newFeed.content;
        this.imageUrl = newFeed.imageUrl;
        this.profileImageUrl = newFeed.profileImageUrl;
    }

    public FeedResponse toResponseDto() {
        return FeedResponse.builder()
            .feedId(feedId)
            .feedNumOfUser(feedNumOfUser)
            .userId(userId)
            .content(content)
            .imageUrl(imageUrl)
            .profileImageUrl(profileImageUrl)
            .countOfGroupFeed(countOfGroupFeed)
            .numOfLikes(numOfLikes)
            .position(position)
            .build();
    }

    public FeedCreateRequestDto toRequestDto() {
        return FeedCreateRequestDto.builder()
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
