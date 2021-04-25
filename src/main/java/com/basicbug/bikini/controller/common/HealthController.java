package com.basicbug.bikini.controller.common;

import com.basicbug.bikini.dto.common.CommonResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/common")
@RequiredArgsConstructor
public class HealthController {

    @ApiOperation(value = "Check server api call status", notes = "API 호출 확인용")
    @GetMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<Void> check() {
        return CommonResponse.empty();
    }
}
