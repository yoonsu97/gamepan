/*
package com.gamepan.gameboard.controller.rest;

import com.gamepan.gameboard.domain.board.dto.BoardRequestDto;
import com.gamepan.gameboard.domain.board.dto.BoardResponseDto;
import com.gamepan.gameboard.domain.board.entity.Board;
import com.gamepan.gameboard.domain.board.service.BoardService;
import com.gamepan.gameboard.domain.comment.dto.CommentResponseDto;
import com.gamepan.gameboard.domain.comment.service.CommentService;
import com.gamepan.gameboard.domain.post.dto.PostResponseDto;
import com.gamepan.gameboard.domain.post.service.PostService;
import com.gamepan.gameboard.domain.user.dto.UserResponseDto;
import com.gamepan.gameboard.domain.user.service.UserService;
import com.gamepan.gameboard.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;
    private final BoardService boardService;
    private final PostService postService;
    private final CommentService commentService;

    // =========================
    //     회원 관리
    // =========================

    //권한이 부여됐는지 확인하는 경로
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllActiveUsers()
                .stream()
                .map(UserResponseDto::from)
                .toList();
        return ResponseEntity.ok(users);
    }

    */
/** 사용자 삭제  *//*

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> softDeleteUser(@PathVariable Long userId) {
        userService.softDeleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    // 사용자 복구
    @PutMapping("/{userId}/restore")
    public ResponseEntity<String> restoreUser(@PathVariable Long userId) {
        userService.restoreUser(userId);
        return ResponseEntity.ok("사용자가 복구되었습니다.");
    }

    // =========================
    //     게시판 관리
    // =========================

    //게시판 전체 가져오기
    @GetMapping("/boards")
    public ResponseEntity<List<BoardResponseDto>> getAllBoards() {
        List<BoardResponseDto> boards = boardService.getAllActiveBoards()
                .stream()
                .map(BoardResponseDto::from)
                .toList();
        return ResponseEntity.ok(boards);
    }

    // 게시판 생성
    @PostMapping("/boards")
    public ResponseEntity<BoardResponseDto> createBoard(@RequestBody BoardRequestDto dto) {
        Board board = boardService.createBoard(dto);
        return ResponseEntity.ok(BoardResponseDto.from(board));
    }

    // 게시판 수정(관리자 권한)
    @PutMapping("/boards/{boardId}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable Long boardId, @RequestBody BoardRequestDto dto) {
        Board updated = boardService.updateBoard(boardId,dto);
        return ResponseEntity.ok(BoardResponseDto.from(updated));
    }

    // 게시판 soft delete (관리자 기능) -  DELETE /boards/{board_id}
    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<Void> softDeleteBoard(@PathVariable Long boardId, @AuthenticationPrincipal CustomUserDetails principal) {
        Long currentUserId = principal.getUser().getId();
        boardService.softDeleteBoard(boardId, currentUserId);
        return ResponseEntity.noContent().build();
    }

    // 게시판 복구
    @PutMapping("/boards/{boardId}/restore")
    public ResponseEntity<String> restoreBoard(@PathVariable Long boardId) {
        boardService.restoreBoard(boardId);
        return ResponseEntity.ok("게시판이 복구되었습니다.");
    }

    // =========================
    //     게시글 / 댓글 관리
    // =========================

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        List<PostResponseDto> posts = postService.getAllActivePosts()
                .stream()
                .map(PostResponseDto::from)
                .toList();
        return ResponseEntity.ok(posts);
    }

    // 게시글 삭제
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> softDeletePost(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails principal) {
        Long currentUserId = principal.getUser().getId();
        postService.softDeletePost(postId, currentUserId);
        return ResponseEntity.noContent().build();
    }

    // 게시글 복구
    @PutMapping("/posts/{postId}/restore")
    public ResponseEntity<String> restorePost(@PathVariable Long postId) {
        postService.restorePost(postId);
        return ResponseEntity.ok("게시글이 복구되었습니다.");
    }

    // 전체 댓글 조회
    @GetMapping("/comments")
    public ResponseEntity<List<CommentResponseDto>> getAllComments() {
        List<CommentResponseDto> comments = commentService.getAllComments()
                .stream()
                .map(CommentResponseDto::from)
                .toList();
        return ResponseEntity.ok(comments);
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
*/
