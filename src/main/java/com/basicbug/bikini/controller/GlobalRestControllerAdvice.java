package com.basicbug.bikini.controller;

import com.basicbug.bikini.dto.common.CommonResponse;
import com.basicbug.bikini.model.auth.AuthError;
import com.basicbug.bikini.model.auth.exception.InvalidAccessTokenException;
import com.basicbug.bikini.model.auth.exception.InvalidProviderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class GlobalRestControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({InvalidProviderException.class})
    public CommonResponse<String> handleBadParameter(Exception exception) {
        log.info("Bad parameter request : ", exception);
        return CommonResponse.error(AuthError.INVALID_PROVIDER.errorCode, AuthError.INVALID_PROVIDER.errorMsg);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({InvalidAccessTokenException.class})
    public CommonResponse<String> handleInvalidAccessToken(Exception exception) {
        log.info("Invalid access token request : ", exception);
        return CommonResponse.error(AuthError.INVALID_ACCESS_TOKEN.errorCode, AuthError.INVALID_ACCESS_TOKEN.errorMsg);
    }
}
