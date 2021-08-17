package com.basicbug.bikini.dto.feed;

import com.basicbug.bikini.model.likes.Likes;
import com.basicbug.bikini.model.likes.TargetType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikesResponseDto {

    private TargetType targetType;
    private String targetId;
    private boolean isLiked = false;

    public LikesResponseDto(TargetType targetType, String targetId, Likes likes) {
        this.targetType = targetType;
        this.targetId = targetId;
        if (likes != null) {
            isLiked = true;
        }
    }
}
