package com.gamepan.gameboard.domain.post.entity;

import com.gamepan.gameboard.domain.board.entity.Board;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
//delete를 할 경우 이 쿼리로 대체하여 보냄
@SQLDelete(sql = "UPDATE post SET is_deleted = true WHERE id = ?")
// 조회할 때 “deleted = false”인 것만 가져옴(관리자 페이지에서는 조정이 필요)
/*@Where(clause = "is_deleted = false")*/
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                   // 게시글 고유 ID

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id",  nullable = false)
    private User user;                   // 작성자 ID

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;                  // 게시판 ID

    @Column(nullable = false, length = 50)
    private String title;                   // 게시글 제목

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

    // 게시글 soft Delete
    public void softDelete() {
        this.isDeleted = true;
    }

    // 게시글 복구
    public void restore() {
        this.isDeleted = false;
    }

    //private LocalDateTime deletedAt;        // 게시글 삭제일(선택)

}
