package com.gamepan.gameboard.domain.post.controller;

import com.gamepan.gameboard.domain.post.dto.PostRequestDto;
import com.gamepan.gameboard.domain.post.dto.PostResponseDto;
import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.post.service.PostService;
import com.gamepan.gameboard.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    //게시글 등록
    @PostMapping("/boards/{boardId}/posts")
    public ResponseEntity<PostResponseDto> createPost(@PathVariable Long boardId,
                                                      @AuthenticationPrincipal CustomUserDetails principal,
                                                      @RequestBody PostRequestDto dto) {
        Long userId = principal.getUser().getId(); // 현재 로그인한 사용자 ID
        Post post = postService.createPost(boardId, userId, dto);
        return ResponseEntity.ok(PostResponseDto.from(post));
    }


    // 게시판 안에 게시물 전체 조회
    @GetMapping("/boards/{boardId}/posts")
    public ResponseEntity<List<PostResponseDto>> getAllPosts(@PathVariable Long boardId) {
        List<PostResponseDto> posts = postService.getAllPosts(boardId)
                .stream()
                .map(PostResponseDto::from)
                .toList();
        return ResponseEntity.ok(posts);
    }

    // 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long id) {
        Post post = postService.getPost(id);
        return ResponseEntity.ok(PostResponseDto.from(post));
    }

    // 수정
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id,
                                                      @AuthenticationPrincipal CustomUserDetails principal,
                                                      @RequestBody PostRequestDto dto) {
        Long currentUserId = principal.getUser().getId();
        Post post = postService.updatePost(id, currentUserId, dto);
        return ResponseEntity.ok(PostResponseDto.from(post));
    }


    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeletePost(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails principal) {
        Long currentUserId = principal.getUser().getId();
        postService.softDeletePost(id, currentUserId);
        return ResponseEntity.noContent().build();
    }

    // 게시글 복구
    @PutMapping("/{id}/restore")
    public ResponseEntity<String> restorePost(@PathVariable Long id) {
        postService.restorePost(id);
        return ResponseEntity.ok("게시글이 복구되었습니다.");
    }
}
