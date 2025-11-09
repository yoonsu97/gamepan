package com.gamepan.gameboard.domain.report.service;

import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.post.repository.PostRepository;
import com.gamepan.gameboard.domain.report.entity.Report;
import com.gamepan.gameboard.domain.report.repository.ReportRepository;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.global.exception.BusinessException;
import com.gamepan.gameboard.global.exception.ErrorCode;
import com.gamepan.gameboard.global.help.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminReportService {
    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final AuthorizationService authorizationService;

    // 관리자: 대기중 신고 목록(게시글 fetch join 포함)
    public List<Report> listPending() {
        return reportRepository.findByStatusOrderByCreatedAtAsc(Report.Status.PENDING);
    }

    // 관리자: 신고 취소(기각에 해당)
    public void cancel(Long reportId, User currentUser) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REPORT_NOT_FOUND));

        authorizationService.hasReportPermission(currentUser, ErrorCode.REPORT_FORBIDDEN);

        report.updateStatus(Report.Status.CANCELED);
    }

    // 관리자: 삭제 확정(신고 인정 + 게시글 소프트 딜리트)
    public void confirmAndDelete(Long reportId, User currentUser) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REPORT_NOT_FOUND));

        Post post = postRepository.findByIdAndIsDeletedFalse(report.getPost().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        authorizationService.hasReportPermission(currentUser, ErrorCode.REPORT_FORBIDDEN);

        // 1) 신고 상태 확정
        report.updateStatus(Report.Status.CONFIRMED);
        // 2) 게시글 소프트 삭제
        post.softDelete();
    }
}
