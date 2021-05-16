package com.basicbug.bikini.model.auth;

import lombok.Getter;

@Getter
public class NaverProfile {

    private String email;
    private String nickname;
    private String profile_image;
    private String age;
    private String gender;
    private String id;
    private String name;
    private String birthday;

    public String getId() {
        return "naver-" + id;
    }
}
