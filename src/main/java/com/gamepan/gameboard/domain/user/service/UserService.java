package com.gamepan.gameboard.domain.user.service;

import com.gamepan.gameboard.domain.user.dto.UserCreateRequestDto;
import com.gamepan.gameboard.domain.user.dto.UserUpdateRequestDto;
import com.gamepan.gameboard.domain.user.entity.Role;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.domain.user.exception.DuplicateEmailException;
import com.gamepan.gameboard.domain.user.exception.DuplicateUsernameException;
import com.gamepan.gameboard.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

// 사용자 관련 비즈니스 로직 처리를 위한 서비스
@Service
@RequiredArgsConstructor //userRepository 의존 주입 생성자 자동 생성
@Transactional // 데이터베이스 관련 작업시 transaction으로 묶어서 작업
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.invite-code:}")   // application-*.yml 에 설정해둔 초대코드
    private String adminInviteCode;

    // Create - 유저 객체 만들기
    public User createUser(UserCreateRequestDto dto, Role role) {
        // 유저 객체 생성전 중복 검사 (username, email)
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateUsernameException();
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException();
        }

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .role(role)
                .build();
        return userRepository.save(user);
    }

    //Read - 사용자 1명 가져오기
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "해당 사용자를 찾을 수 없습니다."));
    }

    //Read - 사용자 모두 가져오기
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //Update - 사용자 정보 바꾸기(password, nickname 변경)
    public User updateUser(Long id, UserUpdateRequestDto dto) {
        User user = getUserById(id);
        // 입력된 비밀번호가 있을 때 비밀번호를 수정
        if(dto.getPassword() != null ){
            user.updatePassword(dto.getPassword(), passwordEncoder);
        }
        // 입력된 닉네임이 있을 때 닉네임을 수정
        if(dto.getNickname() != null){
            user.updateNickname(dto.getNickname());
        }

        return userRepository.save(user);
    }

    //Deleted - 사용자 삭제 (소프트 딜리트)
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    // 권한이 존재하는지 확인 (admin 권한 부여에 사용)
    public boolean existsByRole(Role role) {
        return userRepository.existsByRole(role);
    }

    // Admin 권한 부여
    @Transactional
    public void promoteToAdminByUsername(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디를 찾을 수 없습니다. " + username));
        if (user.getRole() != Role.ADMIN) {
            user.setAdmin();             // 내부에서 role=ADMIN 세팅
            // 변경감지로 flush됨(트랜잭션 활성 상태)
        }
    }

    @Transactional
    public void inviteToAdmin(Long userId, String inviteCode) {

        // 1) 기능 토글/코드 미설정 방어
        if (adminInviteCode == null || adminInviteCode.isBlank()) {
            throw new IllegalStateException("관리자 승격 기능이 비활성화되어 있습니다.");
        }

        // 2) 코드 검증
        if (!adminInviteCode.equals(inviteCode)) {
            throw new IllegalArgumentException("Admin 초대 코드가 올바르지 않습니다.");
        }

        // 3) 유저 조회 및 승격
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setAdmin(); // 내부에서 role = ADMIN 으로 세팅되는 메서드
        // 변경감지로 업데이트 반영
    }
}
