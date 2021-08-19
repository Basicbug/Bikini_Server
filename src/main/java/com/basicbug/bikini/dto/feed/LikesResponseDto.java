package com.basicbug.bikini.dto.feed;

import com.basicbug.bikini.model.likes.TargetType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LikesResponseDto {

    TargetType targetType;
    String targetId;
    boolean isLiked;
}
