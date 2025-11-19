package com.gamepan.gameboard.domain.like.entity;

import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.global.entity.BaseCreatedEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "likes",
        uniqueConstraints = @UniqueConstraint(name = "uk_like_post_user", columnNames = {"post_id", "user_id"})
)
@Builder
public class Like extends BaseCreatedEntity {
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
}
