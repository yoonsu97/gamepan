package com.gamepan.gameboard.domain.board.entity;

import com.gamepan.gameboard.domain.board.dto.BoardRequestDto;
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
@Builder
//delete를 할 경우 이 쿼리로 대체하여 보냄
@Table(name = "boards")
@SQLDelete(sql = "UPDATE board SET is_deleted = true WHERE id = ?")
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


    public void updateAll(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
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
