/*
package com.gamepan.gameboard.domain.like.controller;

import com.gamepan.gameboard.domain.like.service.LikeService;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.global.api.ApiResponse;
import com.gamepan.gameboard.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeController {

    private final LikeService likeService;

    // POST /api/posts/{postId}/likes/toggle
    @PostMapping("/posts/{postId}/likes/toggle")
    public ResponseEntity<ApiResponse<Map<String, Object>>> toggle(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        User user = principal.getUser();
        var result = likeService.toggleLike(postId, user);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        Map.of("liked", result.liked(), "likeCount", result.likeCount()),
                        result.liked() ? "좋아요가 추가되었습니다." : "좋아요가 취소되었습니다."
                )
        );
    }
}*/
