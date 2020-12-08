package com.basicbug.bikini.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RestApiResponse {
    private final String apiStatusCode;
    private final String responseMessage;
}
