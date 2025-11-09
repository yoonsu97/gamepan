package com.gamepan.gameboard.domain.like.entity;

import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "likes",
        uniqueConstraints = @UniqueConstraint(name = "uk_like_post_user", columnNames = {"post_id", "user_id"})
)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== 연관관계 ==== =
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

}
