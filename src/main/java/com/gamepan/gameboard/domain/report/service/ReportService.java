package com.gamepan.gameboard.domain.report.service;

import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.post.repository.PostRepository;
import com.gamepan.gameboard.domain.report.dto.ReportPostRequest;
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
public class ReportService {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;

    // 사용자: 게시글 신고
    public Long submit(Long reporterId, ReportPostRequest req) {
        Post post = postRepository.findByIdAndIsDeletedFalse(req.getPostId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."));

        if (reportRepository.existsByReporterIdAndPost_Id(reporterId, req.getPostId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 신고한 게시글입니다.");
        }

        Report report = Report.builder()
                .reporterId(reporterId)
                .post(post)
                .status(Report.Status.PENDING)
                .build();

        return reportRepository.save(report).getId();
    }

    // 이미 신고한 게시물인지 확인
    @Transactional(readOnly = true)
    public boolean isAlreadyReported(Long userId, Long postId) {
        if (userId == null) return false; // 비로그인 사용자
        return reportRepository.existsByReporterIdAndPost_Id(userId, postId);
    }
}
