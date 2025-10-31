/*
package com.gamepan.gameboard.domain.comment.controller;

import com.gamepan.gameboard.domain.comment.dto.CommentRequestDto;
import com.gamepan.gameboard.domain.comment.dto.CommentResponseDto;
import com.gamepan.gameboard.domain.comment.entity.Comment;
import com.gamepan.gameboard.domain.comment.service.CommentService;
import com.gamepan.gameboard.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long postId,
                                                            @AuthenticationPrincipal CustomUserDetails principal,
                                                            @RequestBody CommentRequestDto dto) {
        Long currentUserId = principal.getUser().getId();
        Comment comment = commentService.createComment(postId, currentUser, dto);
        return ResponseEntity.ok(CommentResponseDto.from(comment));
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
    */
/*@PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto dto) {
        Comment comment = commentService.updateComment(id,dto);
        return ResponseEntity.ok(CommentResponseDto.from(comment));
    }*//*


    // 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
*/
