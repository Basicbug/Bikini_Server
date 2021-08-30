package com.basicbug.bikini.controller.user;

import static com.basicbug.bikini.error.CommonResponseConstant.INVALID_REQUEST;
import static com.basicbug.bikini.error.CommonResponseConstant.SUCCESS;

import com.basicbug.bikini.dto.common.CommonResponse;
import com.basicbug.bikini.dto.user.UserResponseDto;
import com.basicbug.bikini.dto.user.UserUpdateRequestDto;
import com.basicbug.bikini.model.User;
import com.basicbug.bikini.model.auth.exception.UsernameAlreadyExistException;
import com.basicbug.bikini.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "JWT token", dataType = "String", paramType = "header")
    @ApiOperation(value = "Get user profile", notes = "요청 사용자 정보")
    @GetMapping("/about/me")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<UserResponseDto> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();
        log.info("/about/me for ${}", uid);
        return CommonResponse.of(userService.getUserInformation(uid).toDto(), SUCCESS.getCode());
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "JWT token", dataType = "String", paramType = "header")
    @ApiOperation(value = "Update user info", notes = "사용자 정보 업데이트")
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<UserResponseDto> updateUserInfo(@RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();

        log.info("update user information with ${}", uid);
        User updatedUser = userService.updateUserInfo(uid, userUpdateRequestDto);

        return CommonResponse.of(updatedUser.toDto(), SUCCESS.getCode());
    }

    @ExceptionHandler(UsernameAlreadyExistException.class)
    public CommonResponse<String> handleUidAlreadyExists(Exception exception) {
        log.error("Uid is already exists");
        return CommonResponse.error(INVALID_REQUEST.getCode(), "username already exists");
    }
}
