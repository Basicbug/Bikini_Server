package com.basicbug.bikini.model.auth;

public enum AuthProvider {
    NAVER("naver"),
    KAKAO("kakao");

    private final String name;

    AuthProvider(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
