package com.gamepan.gameboard.controller;

import com.gamepan.gameboard.domain.post.dto.PostRequestDto;
import com.gamepan.gameboard.domain.post.dto.PostResponseDto;
import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    //게시글 등록
    @PostMapping("/boards/{boardId}/posts")
    public ResponseEntity<PostResponseDto> createPost(@PathVariable Long boardId, @RequestBody PostRequestDto dto) {
        dto.setBoardId(boardId);
        Post post = postService.createPost(dto);
        return ResponseEntity.ok(PostResponseDto.from(post));
    }

    // 전체 조회
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
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id, @RequestBody PostRequestDto dto) {
        Post post = postService.updatePost(id,dto);
        return ResponseEntity.ok(PostResponseDto.from(post));
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> softDeletePost(@PathVariable Long id) {
        postService.softDeletePost(id);
        return ResponseEntity.ok("게시글이 삭제(soft delete)되었습니다.");
    }

    // 게시글 복구
    @PutMapping("/{id}/restore")
    public ResponseEntity<String> restorePost(@PathVariable Long id) {
        postService.restorePost(id);
        return ResponseEntity.ok("게시글이 복구되었습니다.");
    }
}
