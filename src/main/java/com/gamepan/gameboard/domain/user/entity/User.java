package com.gamepan.gameboard.domain.user.entity;

import com.gamepan.gameboard.domain.comment.entity.Comment;
import com.gamepan.gameboard.domain.like.entity.Like;
import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.report.entity.Report;
import com.gamepan.gameboard.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
//delete를 할 경우 이 쿼리로 대체하여 보냄
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ?")
// 시용자(유저) 엔티티
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                // 사용자 고유  아이디

    @Column(nullable = false, unique = true, length = 20)
    private String username;        // 사용자 로그인 아이디

    @Column(nullable = false)
    private String password;        // 사용자 비밀번호

    @Column(nullable = false, length = 20)
    private String nickname;        // 사용자 닉네임

    @Column(nullable = false, unique = true)
    private String email;           // 사용자 이메일

    @Convert(converter = RoleConverter.class)
    @Column(nullable = false, length = 1)
    private Role role;              // 사용자 권환

    @Column(nullable = false)
    private boolean isDeleted = false;

    // ===== 연관관계 매핑 =====
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();


    public void softDelete() {
        this.isDeleted = true;
    }

    public void restore() {
        this.isDeleted = false;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;;
    }

}
