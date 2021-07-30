package com.basicbug.bikini.error;

import lombok.Getter;

@Getter
public enum CommonResponseConstant {
    SUCCESS(1000, "SUCCESS"),
    FAIL_TO_PROCESS(2000, "FAIL_TO_PROCESS"),
    INVALID_REQUEST(3000, "INVALID_REQUEST");

    private final Integer code;
    private final String message;

    CommonResponseConstant(final Integer code, final String message) {
        this.code = code;
        this.message = message;
    }
}
