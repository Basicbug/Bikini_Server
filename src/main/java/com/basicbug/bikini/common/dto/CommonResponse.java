package com.basicbug.bikini.common.dto;

import com.basicbug.bikini.common.type.CommonResponseCode;
import lombok.Getter;

@Getter
public class CommonResponse<T> {

    private T result;
    private Integer code;
    private String message;

    public CommonResponse(final T result, final Integer code, final String message) {
        this.result = result;
        this.code = code;
        this.message = message;
    }

    public static <Void> CommonResponse<Void> empty() {
        return new CommonResponse<>(null, null, null);
    }

    public static <Void> CommonResponse<Void> error(final CommonResponseCode responseConstant) {
        return new CommonResponse<>(null, responseConstant.getCode(), responseConstant.getMessage());
    }

    public static <Void> CommonResponse<Void> error(final Integer code) {
        return new CommonResponse<>(null, code, null);
    }

    public static <Void> CommonResponse<Void> error(final Integer code, final String message) {
        return new CommonResponse<>(null, code, message);
    }

    public static <T> CommonResponse<T> of(final T result) {
        return new CommonResponse<>(result, null, null);
    }

    public static <T> CommonResponse<T> of(final CommonResponseCode responseConstant) {
        return new CommonResponse<>(null, responseConstant.getCode(), responseConstant.getMessage());
    }

    public static <T> CommonResponse<T> of(final T result, final CommonResponseCode responseConstant) {
        return new CommonResponse<>(result, responseConstant.getCode(), responseConstant.getMessage());
    }

    public static <T> CommonResponse<T> of(final T result, final Integer code) {
        return new CommonResponse<>(result, code, null);
    }

    public static <T> CommonResponse<T> of(final T result, final Integer code, final String message) {
        return new CommonResponse<>(result, code, message);
    }
}
