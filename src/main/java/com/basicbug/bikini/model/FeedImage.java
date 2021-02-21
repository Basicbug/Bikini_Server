package com.basicbug.bikini.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FeedImage {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String url;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
