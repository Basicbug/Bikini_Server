package com.basicbug.bikini.controller.likes;

import com.basicbug.bikini.dto.common.CommonResponse;
import com.basicbug.bikini.dto.feed.LikesRequestDto;
import com.basicbug.bikini.dto.feed.LikesResponseDto;
import com.basicbug.bikini.model.likes.Likes;
import com.basicbug.bikini.model.likes.TargetType;
import com.basicbug.bikini.service.LikesService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/likes")
public class LikesController {

    private final LikesService likesService;

    @ApiOperation(value = "Add likes to target", notes = "지정된 타겟에 좋아요 추가")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "JWT token", required = true, dataType = "String", paramType = "header")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<LikesResponseDto> addLikesToTarget(LikesRequestDto likesRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();
        TargetType targetType = likesRequestDto.getTargetType();
        String targetId = likesRequestDto.getTargetId();

        Likes likes = likesService.addLikesToTarget(targetType, targetId, uid);
        return CommonResponse.of(new LikesResponseDto(targetType, targetId, likes));
    }

    @ApiOperation(value = "Remove likes from Target", notes = "지정 타겟에서 좋아요 제거")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "JWT token", required = true, dataType = "String", paramType = "header")
    @DeleteMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<LikesResponseDto> removeLikesFromTarget(LikesRequestDto likesRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();
        TargetType targetType = likesRequestDto.getTargetType();
        String targetId = likesRequestDto.getTargetId();

        // TODO:qwebnm7788 제거 작업 실패 케이스 처리
        likesService.removeLikesFromTarget(targetType, targetId, uid);

        return CommonResponse.of(new LikesResponseDto(targetType, targetId, null));
    }

    @ApiOperation(value = "Check if given target is liked by user", notes = "지정 타겟 좋아요 여부 확인")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "JWT token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<LikesResponseDto> isFeedLiked(LikesRequestDto likesRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();
        TargetType targetType = likesRequestDto.getTargetType();
        String targetId = likesRequestDto.getTargetId();

        Likes isLiked = likesService.getLikesForTargetByUser(targetType, targetId, uid);
        return CommonResponse.of(new LikesResponseDto(targetType, targetId, isLiked));
    }
}
