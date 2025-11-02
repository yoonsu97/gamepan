package com.gamepan.gameboard.domain.report.controller;

import com.gamepan.gameboard.domain.report.dto.ReportPostRequest;
import com.gamepan.gameboard.domain.report.service.ReportService;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boards/{boardId}/posts/{postId}/report")
public class reportWebController {
    private final ReportService reportService;


    /** 신고 제출 처리 (POST-Redirect-GET) */
    @PostMapping
    public String submit(@AuthenticationPrincipal(expression = "user") User currentUser,
                         @PathVariable Long boardId,
                         @PathVariable Long postId,
                         RedirectAttributes ra) {

        // 로그인 안 되어 있을 경우
        if (currentUser == null) {
            return "redirect:/login";
        }

        // 신고 등록
        ReportPostRequest form = ReportPostRequest.builder()
                .postId(postId)
                .build();

        Long reportId = reportService.submit(currentUser.getId(), form);

        // 결과 메시지 플래시로 전달
        ra.addFlashAttribute("reported", true);
        ra.addFlashAttribute("reportId", reportId);

        // 신고 후 원래 게시글로 리다이렉트
        return "redirect:/boards/" + boardId + "/posts/" + postId + "?noInc=true";
    }
}
