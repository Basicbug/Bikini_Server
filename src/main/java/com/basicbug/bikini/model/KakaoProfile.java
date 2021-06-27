package com.basicbug.bikini.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoProfile {

    private Long id;
    private String username = "Unknown-kakao";

    public String getId() {
        return "kakao-" + id;
    }

}
