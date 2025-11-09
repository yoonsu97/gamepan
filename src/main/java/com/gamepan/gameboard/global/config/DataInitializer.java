package com.gamepan.gameboard.global.config;

import com.gamepan.gameboard.domain.board.entity.Board;
import com.gamepan.gameboard.domain.board.repository.BoardRepository;
import com.gamepan.gameboard.domain.comment.entity.Comment;
import com.gamepan.gameboard.domain.comment.repository.CommentRepository;
import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.post.repository.PostRepository;
import com.gamepan.gameboard.domain.user.entity.Role;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * [설명]
 * - local 환경에서만 실행되는 초기 데이터 삽입 클래스
 * - 애플리케이션 구동 시 DB가 비어있다면 기본 관리자, 게시판, 게시글, 댓글을 자동 생성합니다.
 */
@Slf4j
@Component
@Profile("local") // ⚠️ application-local.yml에서만 실행됨
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("✅ 데이터베이스가 이미 초기화되어 있습니다. 샘플 데이터 생성을 건너뜁니다.");
            return;
        }

        log.info("🚀 샘플 데이터 초기화를 시작합니다...");

        // -------------------------------
        // 1️⃣ 관리자 & 일반 사용자 생성
        // -------------------------------
        User admin = createUser("admin", "admin@example.com", "asdzxc135.", Role.ADMIN);
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            users.add(createUser("user" + i, "user" + i + "@example.com", "12345678!", Role.USER));
        }

        // -------------------------------
        // 2️⃣ 게시판 카테고리 생성
        // -------------------------------
        Board board1 = createBoard("롤 게시판", "롤 게시판 입니다.", "lol");
        Board board2 = createBoard("메이플 게시판", "메이플 게시판 입니다.", "maple");
        Board board3 = createBoard("오버워치 게시판", "오버워치 게시판 입니다.", "overwatch");

        log.info("✅ 샘플 데이터 초기화 완료!");
    }

    // ========================================================================================
    // == Private Helper Methods
    // ========================================================================================

    private User createUser(String username, String email, String password, Role role) {
        return userRepository.save(User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(username)
                .role(role)
                .build());
    }

    private Board createBoard(String name, String description, String code) {
        return boardRepository.save(Board.builder()
                .name(name)
                .description(description)
                .code(code)
                .build());

    }
}

