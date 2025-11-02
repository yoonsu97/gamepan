package com.gamepan.gameboard.domain.like.controller;

import com.gamepan.gameboard.domain.like.service.LikeService;
import com.gamepan.gameboard.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boards/{boardId}/posts/{postId}")
public class LikeWebController {

    private final LikeService likeService;

    /**
     * ❤️ 게시글 좋아요 토글
     * 로그인된 사용자만 가능
     */
    @PostMapping("/like")
    public String toggleLike(@PathVariable Long boardId,
                             @PathVariable Long postId,
                             @AuthenticationPrincipal CustomUserDetails principal) {

        if (principal == null || principal.getUser() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        likeService.toggleLike(postId, principal.getUser());

        // 좋아요 클릭 후 다시 게시글 상세 페이지로 리다이렉트
        return "redirect:/boards/" + boardId + "/posts/" + postId + "?noInc=true";
    }
}
