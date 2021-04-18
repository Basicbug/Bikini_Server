package com.basicbug.bikini.model;

import com.basicbug.bikini.dto.feed.FeedCreateRequestDto;
import com.basicbug.bikini.dto.feed.FeedResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Builder
@Getter
@Setter
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

    @OneToMany(mappedBy = "feed", cascade = CascadeType.PERSIST)
    private List<FeedImage> images = new ArrayList<>();

    private String profileImageUrl;

    @Embedded
    private Location location;

    public void update(Feed newFeed) {
        this.feedNumOfUser = newFeed.feedNumOfUser;
        this.countOfGroupFeed = newFeed.countOfGroupFeed;
        this.numOfLikes = newFeed.numOfLikes;
        this.userId = newFeed.userId;
        this.content = newFeed.content;
        this.imageUrl = newFeed.imageUrl;
        this.profileImageUrl = newFeed.profileImageUrl;
        this.location = newFeed.location;
    }

    public FeedResponse toResponseDto() {
        return FeedResponse.builder()
            .feedId(feedId)
            .feedNumOfUser(feedNumOfUser)
            .userId(userId)
            .content(content)
            .imageUrl(images.stream().map(FeedImage::getUrl).collect(Collectors.toList()))
            .profileImageUrl(profileImageUrl)
            .countOfGroupFeed(countOfGroupFeed)
            .numOfLikes(numOfLikes)
            .location(location)
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
            .location(location)
            .build();
    }
}
