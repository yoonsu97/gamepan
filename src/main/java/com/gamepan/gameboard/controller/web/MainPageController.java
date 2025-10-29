package com.gamepan.gameboard.controller.web;

import com.gamepan.gameboard.domain.board.entity.Board;
import com.gamepan.gameboard.domain.board.service.BoardService;
import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/gamepan")
public class MainPageController {
    private final BoardService boardService;
    private final PostService postService;


    /**
     * 메인 페이지
     * - 상단: 프로젝트명, 오른쪽: 로그인 버튼(or 마이페이지/로그아웃)
     * - 본문: 게시판 별로 최신 글 N개
     */
    @GetMapping
    public String index(Model model) {

        List<Board> boards = boardService.getAllActiveBoards(); // 공개 게시판만 가져오도록 구현 추천
        Map<Long, List<Post>> postsByBoard = boards.stream()
                .collect(Collectors.toMap(
                        Board::getId,
                        b -> postService.getRecentPostsByBoard(b.getId())
                ));

        model.addAttribute("boards", boards);
        model.addAttribute("postsByBoard", postsByBoard);

        // 로그인 사용자 노출이 필요하면(세션/스프링시큐리티):
        // model.addAttribute("currentUser", authService.getCurrentUserOrNull());

        return "main/index"; // templates/index.html
    }
}
