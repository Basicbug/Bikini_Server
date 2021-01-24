package com.basicbug.bikini.dto.auth;

import com.basicbug.bikini.model.NaverUser;
import lombok.Getter;

@Getter
public class NaverProfileResponseDto {

    private String resultCode;
    private String message;
    private NaverUser response;
}
