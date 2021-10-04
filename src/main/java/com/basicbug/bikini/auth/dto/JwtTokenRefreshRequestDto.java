package com.basicbug.bikini.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class JwtTokenRefreshRequestDto {
    String accessToken;
    String refreshToken;
}
