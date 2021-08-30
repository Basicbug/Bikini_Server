package com.basicbug.bikini.dto.user;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserInfo {
    private String username;
}
