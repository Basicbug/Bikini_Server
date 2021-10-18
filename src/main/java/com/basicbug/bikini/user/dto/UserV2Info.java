package com.basicbug.bikini.user.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserV2Info {
    String username;
    long feedCount;
    long likeCount;
}
