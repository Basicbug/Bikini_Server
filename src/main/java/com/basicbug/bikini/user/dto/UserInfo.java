package com.basicbug.bikini.user.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserInfo {
    private String username;
}
