package com.basicbug.bikini.dto.feed;

import com.basicbug.bikini.model.Likes;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikesResponseDto {

    private String feedId;
    private boolean isLiked = false;

    public LikesResponseDto(String feedId, Likes likes) {
        this.feedId = feedId;
        if (likes != null) {
            isLiked = true;
        }
    }
}
