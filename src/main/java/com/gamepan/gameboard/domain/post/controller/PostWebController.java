package com.gamepan.gameboard.domain.post.controller;

import com.gamepan.gameboard.domain.board.service.BoardService;
import com.gamepan.gameboard.domain.comment.service.CommentService;
import com.gamepan.gameboard.domain.post.dto.PostRequestDto;
import com.gamepan.gameboard.domain.post.dto.PostResponseDto;
import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.post.service.PostService;
import com.gamepan.gameboard.domain.report.service.ReportService;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@Controller
@RequiredArgsConstructor
@RequestMapping("/boards/{boardId}/posts")
public class PostWebController {

    private final PostService postService;
    private final CommentService commentService;
    private final BoardService boardService;
    private final ReportService reportService;


    // 게시글 등록 폼
    @GetMapping("/create")
    public String showCreateForm(@PathVariable Long boardId, Model model) {
        model.addAttribute("board", boardService.getBoard(boardId));
        model.addAttribute("postRequestDto", new PostRequestDto());
        return "post/create"; // templates/post/create.html
    }

    //게시글 등록
    @PostMapping
    public String createPost(@PathVariable Long boardId,
                             @AuthenticationPrincipal CustomUserDetails principal,
                             @ModelAttribute PostRequestDto dto) {
        if (principal == null || principal.getUser() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        Long userId = principal.getUser().getId();
        postService.createPost(boardId, userId, dto);

        return "redirect:/boards/" + boardId + "/posts";
    }


    // 게시글 조회
    @GetMapping
    public String getAllPosts(@PathVariable Long boardId, Model model) {
        model.addAttribute("board", boardService.getBoard(boardId));
        model.addAttribute("posts", postService.getAllPosts(boardId));
        return "post/list"; // templates/post/list.html
    }

    // 상세 조회
    @GetMapping("/{postId}")
    public String getPostDetail(@PathVariable Long boardId,
                                @PathVariable Long postId,
                                @AuthenticationPrincipal(expression = "user") User currentUser,
                                Model model) {
        model.addAttribute("board", boardService.getBoard(boardId));
        model.addAttribute("post", postService.getPost(postId));
        model.addAttribute("comments", commentService.getCommentsByPost(postId));

        boolean alreadyReported = reportService.isAlreadyReported(currentUser.getId(), postId);
        model.addAttribute("alreadyReported", alreadyReported);

        return "post/detail"; // templates/post/detail.html
    }


    // 수정 폼
    @GetMapping("/{postId}/edit")
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
    @PostMapping("/{postId}/edit")
    public String updatePost(@PathVariable Long boardId,
                             @PathVariable Long postId,
                             @AuthenticationPrincipal(expression = "user") User currentUser,
                             @ModelAttribute PostRequestDto dto) {
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        postService.updatePost(postId, currentUser, dto);

        return "redirect:/boards/" + boardId + "/posts/" + postId;
    }


    // 삭제
    @PostMapping("/{postId}/delete")
    public String deletePost(@PathVariable Long boardId,
                             @PathVariable Long postId,
                             @AuthenticationPrincipal(expression = "user") User currentUser) {
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        postService.softDeletePost(postId, currentUser);

        return "redirect:/boards/" + boardId + "/posts";
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
