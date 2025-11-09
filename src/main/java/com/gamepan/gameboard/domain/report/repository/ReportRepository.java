package com.gamepan.gameboard.domain.report.repository;

import com.gamepan.gameboard.domain.report.entity.Report;
import com.gamepan.gameboard.domain.report.entity.Report.Status;
import org.springframework.data.jpa.repository.*;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    boolean existsByUser_IdAndPost_Id(Long userId, Long postId);

    @EntityGraph(attributePaths = {"post"})
    List<Report> findByStatusOrderByCreatedAtAsc(Status status);

    long countByPost_IdAndStatus(Long postId, Status status);
}
