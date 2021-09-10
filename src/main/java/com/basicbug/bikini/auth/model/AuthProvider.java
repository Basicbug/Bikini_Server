package com.basicbug.bikini.auth.model;

import com.basicbug.bikini.auth.exception.InvalidProviderException;
import java.util.Arrays;

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

    public static AuthProvider of(String provider) {
        return Arrays.stream(AuthProvider.values())
            .filter(authProvider -> authProvider.name.equalsIgnoreCase(provider))
            .findAny()
            .orElseThrow(() -> new InvalidProviderException("해당하는 provider 가 없습니다."));
    }
}
