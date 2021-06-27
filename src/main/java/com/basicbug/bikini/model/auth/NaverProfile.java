package com.basicbug.bikini.model.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NaverProfile {

    private String email;
    private String username = "Unknown-naver";
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
