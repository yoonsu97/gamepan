package com.gamepan.gameboard.controller.web;

import com.gamepan.gameboard.domain.board.dto.BoardRequestDto;
import com.gamepan.gameboard.domain.board.service.AdminBoardService;
import com.gamepan.gameboard.domain.comment.service.CommentService;
import com.gamepan.gameboard.domain.post.service.PostService;
import com.gamepan.gameboard.domain.report.service.AdminReportService;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.domain.user.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminWebController {

    private final AdminUserService adminUserService;
    private final AdminBoardService adminBoardService;
    private final AdminReportService adminreportService;
    private final PostService postService;
    private final CommentService commentService;

    //     대시보드
    @GetMapping
    public String dashboard() {
        return "admin/index"; // templates/admin/index.html
    }

    //     회원 관리 (조회/삭제/복구)
    @GetMapping("/users")
    public String userList(Model model) {
        model.addAttribute("users", adminUserService.getAllActiveUsers());
        return "admin/user/list"; // templates/admin/user/list.html
    }

    @PostMapping("/users/delete")
    public String userDelete(@RequestParam(name = "userIds", required = false) List<Long> userIds,
                             RedirectAttributes ra) {
        int affected = adminUserService.deleteUsers(userIds);
        ra.addFlashAttribute("message", affected > 0 ? affected + "명 삭제 완료" : "삭제할 사용자가 없습니다.");
        return "redirect:/admin/users";
    }

    @GetMapping("/deletedUsers")
    public String deletedUserList(Model model) {
        model.addAttribute("users", adminUserService.getAllDeletedUsers());
        return "admin/user/deleted-list"; // templates/admin/user/deleted-list.html
    }

    @PostMapping("/deletedUsers/restore")
    public String restoreUsers(@RequestParam(name = "userIds", required = false) List<Long> userIds,
                               RedirectAttributes ra) {
        int affected = adminUserService.restoreUsers(userIds);
        ra.addFlashAttribute("message", affected > 0 ? affected + "명 복구 완료" : "복구할 사용자가 없습니다.");
        return "redirect:/admin/deletedUsers";
    }

    //   게시판 관리 (생성/수정/삭제/복구)
    @GetMapping("/boards")
    public String boardList(Model model) {
        model.addAttribute("boards", adminBoardService.getAllActiveBoards());
        return "admin/board/list";
    }

    @GetMapping("/boards/new")
    public String newForm() {
        return "admin/board/new";
    }

    @PostMapping("/boards/create")
    public String createBoard(@Valid @ModelAttribute("boardForm") BoardRequestDto dto,
                              RedirectAttributes ra) {
        adminBoardService.createBoard(dto);
        ra.addFlashAttribute("message", "게시판이 생성되었습니다.");
        return "redirect:/admin/boards";
    }

    @GetMapping("/boards/{boardId}/edit")
    public String editForm(@PathVariable Long boardId, Model model) {
        model.addAttribute("board", adminBoardService.getBoard(boardId));
        return "admin/board/edit";
    }

    @PostMapping("/boards/{boardId}/update")
    public String updateBoard(@PathVariable Long boardId,
                              @Valid @ModelAttribute("boardForm") BoardRequestDto dto,
                              RedirectAttributes ra) {
        adminBoardService.updateBoard(boardId, dto);
        ra.addFlashAttribute("message", "게시판이 수정되었습니다.");
        return "redirect:/admin/boards";
    }

    @PostMapping("/boards/{boardId}/delete")
    public String deleteBoard(@PathVariable Long boardId,
                              @AuthenticationPrincipal(expression = "user") User currentUser,
                              RedirectAttributes ra) {
        adminBoardService.softDeleteBoard(boardId,currentUser.getId()); // 관리자 전용 삭제
        ra.addFlashAttribute("message", "게시판이 삭제되었습니다.");
        return "redirect:/admin/boards";
    }

    @GetMapping("/deletedBoards")
    public String deletedBoardList(Model model) {
        model.addAttribute("boards", adminBoardService.getAllDeletedBoards());
        return "admin/board/deleted-list"; // templates/admin/board/deleted-list.html
    }

    @PostMapping("/deletedBoards/{boardId}/restore")
    public String restoreBoard(@PathVariable Long boardId, RedirectAttributes ra) {
        adminBoardService.restoreBoard(boardId); // 하위 게시글 복구 정책은 서비스에서 처리
        ra.addFlashAttribute("message", "게시판이 복구되었습니다.");
        return "redirect:/admin/deletedBoards?status=deleted";
    }

    // 신고 관리 (게시글)
    /* 신고 목록 화면 */
    @GetMapping("/reports/posts")
    public String pendingList(Model model) {
        model.addAttribute("pendingReports", adminreportService.listPending());
        return "admin/report/report-post-list";
    }

    /* 신고 취소  */
    @PostMapping("/reports/posts/{reportId}/cancel")
    public String cancel(@PathVariable Long reportId, RedirectAttributes ra) {
        adminreportService.cancel(reportId);
        ra.addFlashAttribute("toast", "신고를 취소했습니다.");
        return "redirect:/admin/reports/posts";
    }

    /* 삭제 확정 (게시글 삭제) */
    @PostMapping("/reports/posts/{reportId}/confirm")
    public String confirmAndDelete(@PathVariable Long reportId, RedirectAttributes ra) {
        adminreportService.confirmAndDelete(reportId);
        ra.addFlashAttribute("toast", "신고 승인 및 게시글을 삭제했습니다.");
        return "redirect:/admin/reports/posts";
    }
}