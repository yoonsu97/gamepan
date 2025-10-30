package com.gamepan.gameboard.domain.report.service;

import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.post.repository.PostRepository;
import com.gamepan.gameboard.domain.report.entity.Report;
import com.gamepan.gameboard.domain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminReportService {
    private final ReportRepository reportRepository;
    private final PostRepository postRepository;

    // 관리자: 대기중 신고 목록(게시글 fetch join 포함)
    public List<Report> listPending() {
        return reportRepository.findByStatusOrderByCreatedAtAsc(Report.Status.PENDING);
    }

    // 관리자: 신고 취소(기각에 해당)
    public void cancel(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "신고를 찾을 수 없습니다."));
        report.setStatus(Report.Status.CANCELED);
    }

    // 관리자: 삭제 확정(신고 인정 + 게시글 소프트 딜리트)
    public void confirmAndDelete(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "신고를 찾을 수 없습니다."));

        Post post = postRepository.findByIdAndIsDeletedFalse(report.getPost().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.GONE, "이미 삭제된 게시글입니다."));

        // 1) 신고 상태 확정
        report.setStatus(Report.Status.CONFIRMED);
        // 2) 게시글 소프트 삭제
        post.softDelete();
        postRepository.save(post);
    }
}
