package com.gamepan.gameboard.domain.board.entity;

import com.gamepan.gameboard.domain.post.entity.Post;
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
@Setter
@Builder
//delete를 할 경우 이 쿼리로 대체하여 보냄
@SQLDelete(sql = "UPDATE board SET is_deleted = true WHERE id = ?")
// 조회할 때 “deleted = false”인 것만 가져옴(관리자 페이지에서는 조정이 필요)
/*@Where(clause = "is_deleted = false")*/
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                //게시판 고유ID

    @Column(nullable = false, unique = true, length = 30)
    private String code;               // 게시판 코드

    @Column(nullable = false,length = 50)
    private String name;            // 게시판 이름

    @Column(length = 500)
    private String description;     // 게시판 설명

    // JPA 접근용 getter (Hibernate에서 Where절 및 쿼리 시 사용)
    // soft delete
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    // ===== 연관관계 편의 메서드 =====
    public void addPost(Post post) {
        posts.add(post);
        post.setBoard(this);
    }

    // 게시판 삭제 시 게시글도 Soft Delete
    public void softDelete() {
        this.isDeleted = true;

    }

    // 복구 시 게시글도 함께 복구
    public void restore() {
        this.isDeleted = false;

    }

    public boolean isDeleted() {
        return isDeleted;
    }


    /*
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isPublic = true; // 공개여부 보류
    */
}
