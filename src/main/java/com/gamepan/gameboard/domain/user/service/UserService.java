package com.gamepan.gameboard.domain.user.service;

import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.user.dto.UserCreateRequestDto;
import com.gamepan.gameboard.domain.user.dto.UserUpdateRequestDto;
import com.gamepan.gameboard.domain.user.entity.Role;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.domain.user.exception.DuplicateEmailException;
import com.gamepan.gameboard.domain.user.exception.DuplicateUsernameException;
import com.gamepan.gameboard.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    //  삭제 (Soft Delete 적용)
    public void softDeleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 사용자가 존재하지 않습니다."));

        user.softDelete(); // BaseEntity의 softDelete() 메서드 호출
        userRepository.save(user);
    }

    //  복구
    public void restoreUser(Long id) {
        User user = userRepository.findByIdIncludingDeleted(id)
                .orElseThrow(() -> new IllegalArgumentException("복구할 사용자가 존재하지 않습니다."));

        user.restore(); // BaseEntity의 restore() 메서드 호출
        userRepository.save(user);
    }
}
