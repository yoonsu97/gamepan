package com.gamepan.gameboard.domain.post.entity;

import com.gamepan.gameboard.domain.board.entity.Board;
import com.gamepan.gameboard.domain.comment.entity.Comment;
import com.gamepan.gameboard.domain.like.entity.Like;
import com.gamepan.gameboard.domain.report.entity.Report;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
//delete를 할 경우 이 쿼리로 대체하여 보냄
@Table(name = "posts")
@SQLDelete(sql = "UPDATE post SET is_deleted = true WHERE id = ?")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                   // 게시글 고유 ID

    // ===== 연관관계 =====
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(nullable = false, length = 50)
    private String title;                   // 게시글  제목

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;                 // 게시글 본문

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0") // 마이너스 없애기
    private int viewCount;                  // 게시글 조회 수

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0") // 마이너스 없애기
    private int likeCount;                  // 게시글 좋아요 수

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0") // 마이너스 없애기
    private int commentCount;               // 게시글 댓글 수


    @Column(nullable = false)
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    // 게시글 soft Delete
    public void softDelete() {
        this.isDeleted = true;
    }

    // 게시글 복구
    public void restore() {
        this.isDeleted = false;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public void updateLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public void updateCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    //private LocalDateTime deletedAt;        // 게시글 삭제일(선택)

}
