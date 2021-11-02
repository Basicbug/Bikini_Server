package com.basicbug.bikini.feed.model;

import com.basicbug.bikini.common.model.BaseEntity;
import com.basicbug.bikini.feed.dto.FeedCreateRequestDto;
import com.basicbug.bikini.feed.dto.FeedResponse;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.basicbug.bikini.user.model.User;
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
public class Feed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(columnDefinition = "VARCHAR(36)", nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Builder.Default
    private UUID feedId = UUID.randomUUID();

    private int feedNumOfUser;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    private String imageUrl;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<FeedImage> images = new ArrayList<>();

    @Embedded
    private Location location;

    public void update(Feed newFeed) {
        this.feedNumOfUser = newFeed.feedNumOfUser;
        this.content = newFeed.content;
        this.imageUrl = newFeed.imageUrl;
        this.location = newFeed.location;
    }

    public FeedResponse toResponseDto(int numOfLikes, boolean isLiked) {
        return FeedResponse.builder()
            .feedId(feedId)
            .feedNumOfUser(feedNumOfUser)
            .username(user.getNickname())
            .content(content)
            .imageUrl(images.stream().map(FeedImage::getUrl).collect(Collectors.toList()))
            .location(location)
            .numOfLikes(numOfLikes)
            .isLiked(isLiked)
            .modifiedAt(getModifiedAt())
            .build();
    }

    public FeedCreateRequestDto toRequestDto() {
        return FeedCreateRequestDto.builder()
            .feedNumOfUser(feedNumOfUser)
            .content(content)
            .imageUrl(imageUrl)
            .location(location)
            .build();
    }
}
