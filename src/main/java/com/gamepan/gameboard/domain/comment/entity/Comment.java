package com.gamepan.gameboard.domain.comment.entity;

import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== 연관관계 ==== =
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column( length = 500, nullable = false)
    private String content;
}
