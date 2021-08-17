package com.basicbug.bikini.dto.feed;

import com.basicbug.bikini.model.likes.TargetType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikesRequestDto {

    TargetType targetType;
    String targetId;
}
