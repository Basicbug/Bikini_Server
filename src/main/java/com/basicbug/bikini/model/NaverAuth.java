package com.basicbug.bikini.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NaverAuth {

    private String access_token;
    private String refresh_token;
    private String token_type;
    private long expires_in;
}
