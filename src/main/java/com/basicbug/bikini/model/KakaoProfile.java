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
    private Properties properties;

    public String getId() {
        return "kakao-" + id;
    }

    @Getter
    @Setter
    public static class Properties {
        private String nickname = "Unknown-kakao";

        public String getNickName() {
            return nickname;
        }
    }
}
