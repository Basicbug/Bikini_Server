package com.basicbug.bikini.dto.auth;

import com.basicbug.bikini.model.auth.NaverProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NaverProfileResponseDto {

    private String resultCode;
    private String message;
    private NaverProfile response;
}
