package com.gamepan.gameboard.domain.post.controller;

import com.gamepan.gameboard.domain.board.service.BoardService;
import com.gamepan.gameboard.domain.comment.service.CommentService;
import com.gamepan.gameboard.domain.post.dto.PostRequestDto;
import com.gamepan.gameboard.domain.post.dto.PostResponseDto;
import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.post.service.PostService;
import com.gamepan.gameboard.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts/web")
public class PostWebController {

    private final BoardService boardService;
    private final PostService postService;
    private final CommentService commentService;


    // 게시글 등록 폼
    @GetMapping("/boards/{boardId}/posts/create")
    public String showCreateForm(@PathVariable Long boardId, Model model) {
        model.addAttribute("board", boardService.getBoard(boardId));
        model.addAttribute("postRequestDto", new PostRequestDto());
        return "post/create"; // templates/post/create.html
    }

    //게시글 등록
    @PostMapping("/boards/{boardId}/posts")
    public String createPost(@PathVariable Long boardId,
                             @AuthenticationPrincipal CustomUserDetails principal,
                             @ModelAttribute PostRequestDto dto) {
        if (principal == null || principal.getUser() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        Long userId = principal.getUser().getId();
        postService.createPost(boardId, userId, dto);

        return "redirect:/posts/web/boards/" + boardId + "/posts";
    }


    // 게시글 조회
    @GetMapping("/boards/{boardId}/posts")
    public String getAllPosts(@PathVariable Long boardId, Model model) {
        model.addAttribute("board", boardService.getBoard(boardId));
        model.addAttribute("posts", postService.getAllPosts(boardId));
        return "post/list"; // templates/post/list.html
    }

    // 상세 조회
    @GetMapping("/boards/{boardId}/posts/{postId}")
    public String getPostDetail(@PathVariable Long boardId,
                                @PathVariable Long postId,
                                Model model) {
        model.addAttribute("board", boardService.getBoard(boardId));
        model.addAttribute("post", postService.getPost(postId));
        model.addAttribute("comments", commentService.getCommentsByPost(postId));
        return "post/detail"; // templates/post/detail.html
    }


    // 수정 폼
    @GetMapping("/boards/{boardId}/posts/{postId}/edit")
    public String showEditForm(@PathVariable Long boardId,
                               @PathVariable Long postId,
                               Model model) {
        Post post = postService.getPost(postId);

        model.addAttribute("board", boardService.getBoard(boardId));

        model.addAttribute("boardId", boardId);
        model.addAttribute("postId", postId);
        model.addAttribute("postRequestDto", new PostRequestDto(post.getTitle(), post.getContent()));
        return "post/edit";
    }

    // 수정
    @PostMapping("/boards/{boardId}/posts/{postId}/edit")
    public String updatePost(@PathVariable Long boardId,
                             @PathVariable Long postId,
                             @AuthenticationPrincipal CustomUserDetails principal,
                             @ModelAttribute PostRequestDto dto) {
        if (principal == null || principal.getUser() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        Long currentUserId = principal.getUser().getId();
        postService.updatePost(postId, currentUserId, dto);

        return "redirect:/posts/web/boards/" + boardId + "/posts/" + postId;
    }


    // 삭제
    @PostMapping("/boards/{boardId}/posts/{postId}/delete")
    public String deletePost(@PathVariable Long boardId,
                             @PathVariable Long postId,
                             @AuthenticationPrincipal CustomUserDetails principal) {
        if (principal == null || principal.getUser() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        Long currentUserId = principal.getUser().getId();
        postService.softDeletePost(postId, currentUserId);

        return "redirect:/posts/web/boards/" + boardId + "/posts";
    }

    /*
    // 게시글 복구
    @PutMapping("/{id}/restore")
    public ResponseEntity<String> restorePost(@PathVariable Long id) {
        postService.restorePost(id);
        return ResponseEntity.ok("게시글이 복구되었습니다.");
    }
     */
}
