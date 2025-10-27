package com.gamepan.gameboard.domain.board.entity;

import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
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

    /*
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isPublic = true; // 공개여부 보류
    */
}
