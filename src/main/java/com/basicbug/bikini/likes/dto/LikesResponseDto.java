package com.basicbug.bikini.likes.dto;

import com.basicbug.bikini.likes.type.TargetType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LikesResponseDto {

    TargetType targetType;
    String targetId;
    boolean isLiked;
}
