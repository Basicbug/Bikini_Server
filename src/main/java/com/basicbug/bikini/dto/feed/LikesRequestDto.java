package com.basicbug.bikini.dto.feed;

import com.basicbug.bikini.model.likes.TargetType;
import lombok.Value;

@Value
public class LikesRequestDto {

    TargetType targetType;
    String targetId;
}
