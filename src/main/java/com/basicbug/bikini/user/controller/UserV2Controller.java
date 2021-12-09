package com.basicbug.bikini.user.controller;

import com.basicbug.bikini.common.dto.CommonResponse;
import com.basicbug.bikini.common.type.CommonResponseCode;
import com.basicbug.bikini.user.dto.UserUpdateRequestDto;
import com.basicbug.bikini.user.dto.UserV2ResponseDto;
import com.basicbug.bikini.user.model.User;
import com.basicbug.bikini.user.service.UserV2Service;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/user")
public class UserV2Controller {

    private final UserV2Service userV2Service;

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "JWT token", dataType = "String", paramType = "header")
    @ApiOperation(value = "[v2] Get user profile", notes = "요청 사용자 정보")
    @GetMapping("/about/me")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<UserV2ResponseDto> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();
        log.info("/v2/user/about/me for {}", uid);

        return CommonResponse.of(
                new UserV2ResponseDto(userV2Service.getUserInfo(uid)),
                CommonResponseCode.SUCCESS.getCode());
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "JWT token", dataType = "String", paramType = "header")
    @ApiOperation(value = "[v2] Update user profile", notes = "사용자 정보 수정")
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<Void> updateUserInfo(@RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();

        User newUser = userUpdateRequestDto.toUserEntity();
        log.info("update user information with ${}", uid);

        userV2Service.updateUserInfo(uid, newUser);
        return CommonResponse.empty();
    }
}
