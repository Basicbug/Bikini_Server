package com.basicbug.bikini.common.advice;

import com.basicbug.bikini.common.dto.CommonResponse;
import com.basicbug.bikini.auth.model.AuthError;
import com.basicbug.bikini.auth.exception.InvalidAccessTokenException;
import com.basicbug.bikini.auth.exception.InvalidProviderException;
import com.basicbug.bikini.auth.exception.UserNotFoundException;
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UserNotFoundException.class})
    public CommonResponse<String> handleUserNotFound(Exception exception) {
        log.info("User is not exists : {}", exception.getMessage());
        return CommonResponse.error(HttpStatus.BAD_REQUEST.value(), "User is not exists");
    }
}
