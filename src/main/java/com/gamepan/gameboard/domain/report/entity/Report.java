package com.gamepan.gameboard.domain.report.entity;

import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "report",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_report_post_unique_per_user",
                columnNames = {"reporter_id", "post_id"}
        ),
        indexes = {
                @Index(name = "idx_report_posts_status", columnList = "status"),
                @Index(name = "idx_report_posts_post", columnList = "post_id")
        }
)
public class Report extends BaseEntity {
    // 상태 (신고 대기, 신고 취소, 삭제 확정 )
    public enum Status { PENDING, CANCELED, CONFIRMED }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reporter_id", nullable = false)
    private Long reporterId;        // 신고한 사용자 ID

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id")
    private Post post;              // 신고 대상 게시글

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Status status = Status.PENDING;
}
