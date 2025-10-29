package com.gamepan.gameboard.domain.user.entity;

import com.gamepan.gameboard.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.security.crypto.password.PasswordEncoder;


@Entity
@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
//delete를 할 경우 이 쿼리로 대체하여 보냄
@SQLDelete(sql = "UPDATE user SET is_deleted = true WHERE id = ?")
// 조회할 때 “is_deleted = false”인 것만 가져옴(관리자 페이지에서는 조정이 필요)
/*@Where(clause = "is_deleted = false")*/
// 시용자(유저) 엔티티
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                // 사용자 고유 아이디

    @Column(nullable = false, unique = true, length = 20)
    private String username;        // 사용자 로그인 아이디

    @Column(nullable = false)
    private String password;        // 사용자 비밀번호

    @Column(nullable = false, length = 20)
    private String nickname;        // 사용자 닉네임

    @Column(nullable = false, unique = true)
    private String email;           // 사용자 이메일

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;              // 사용자 권환

    @Column(nullable = false)
    private boolean isDeleted = false;

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

    public void setAdmin(){
        role =  Role.ADMIN;
    }
}
