package com.gamepan.gameboard.domain.comment.controller;

import com.gamepan.gameboard.domain.comment.dto.CommentRequestDto;
import com.gamepan.gameboard.domain.comment.dto.CommentResponseDto;
import com.gamepan.gameboard.domain.comment.entity.Comment;
import com.gamepan.gameboard.domain.comment.service.CommentService;
import com.gamepan.gameboard.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boards/{boardId}/posts/{postId}/comments")
public class CommentWebController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping
    public String createComment(@PathVariable Long boardId,
                                @PathVariable Long postId,
                                @AuthenticationPrincipal CustomUserDetails principal,
                                @ModelAttribute CommentRequestDto dto) {

        if (principal == null || principal.getUser() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        Long currentUserId = principal.getUser().getId();
        commentService.createComment(postId, currentUserId, dto);

        // 등록 후 게시글 상세페이지로 리다이렉트
        return "redirect:/boards/" + boardId + "/posts/" + postId;
    }

    // 게시글별 댓글 조회
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPost(@PathVariable Long postId) {
        List<CommentResponseDto> comments = commentService.getCommentsByPost(postId)
                .stream()
                .map(CommentResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(comments);
    }

    // 댓글 수정
    @PostMapping("/{commentId}/edit")
    public String updateComment(@PathVariable Long boardId,
                                @PathVariable Long postId,
                                @PathVariable Long commentId,
                                @AuthenticationPrincipal CustomUserDetails principal,
                                @ModelAttribute CommentRequestDto dto) {

        if (principal == null || principal.getUser() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        Long currentUserId = principal.getUser().getId();
        commentService.updateComment(commentId, currentUserId, dto);

        return "redirect:/boards/" + boardId + "/posts/" + postId;
    }

    // 댓글 삭제
    @PostMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable Long boardId,
                                @PathVariable Long postId,
                                @PathVariable Long commentId,
                                @AuthenticationPrincipal CustomUserDetails principal) {

        if (principal == null || principal.getUser() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        Long currentUserId = principal.getUser().getId();
        commentService.deleteComment(commentId, currentUserId);

        return "redirect:/boards/" + boardId + "/posts/" + postId;
    }
}
