package com.basicbug.bikini.likes.dto;

import com.basicbug.bikini.likes.type.TargetType;
import lombok.Value;

@Value
public class LikesRequestDto {

    TargetType targetType;
    String targetId;
}
