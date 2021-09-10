package com.basicbug.bikini.auth.dto;

import com.basicbug.bikini.auth.model.NaverProfile;
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
