package com.gamepan.gameboard.domain.report.service;

import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.post.repository.PostRepository;
import com.gamepan.gameboard.domain.report.dto.ReportPostRequest;
import com.gamepan.gameboard.domain.report.entity.Report;
import com.gamepan.gameboard.domain.report.repository.ReportRepository;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.domain.user.repository.UserRepository;
import com.gamepan.gameboard.global.exception.BusinessException;
import com.gamepan.gameboard.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 사용자: 게시글 신고
    public Long submit(Long userId, ReportPostRequest req) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findByIdAndIsDeletedFalse(req.getPostId())
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (reportRepository.existsByUser_IdAndPost_Id(userId, req.getPostId())) {
            throw new BusinessException(ErrorCode.REPORT_POST_DUPLICATE);
        }

        Report report = Report.builder()
                .user(user)
                .post(post)
                .status(Report.Status.PENDING)
                .build();

        return reportRepository.save(report).getId();
    }

    // 이미 신고한 게시물인지 확인
    @Transactional(readOnly = true)
    public boolean isAlreadyReported(Long userId, Long postId) {
        if (userId == null) return false; // 비로그인 사용자
        return reportRepository.existsByUser_IdAndPost_Id(userId, postId);
    }
}
