package com.gamepan.gameboard.domain.user.service;

import com.gamepan.gameboard.domain.user.dto.UserCreateRequestDto;
import com.gamepan.gameboard.domain.user.entity.Role;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.domain.user.exception.DuplicateEmailException;
import com.gamepan.gameboard.domain.user.exception.DuplicateUsernameException;
import com.gamepan.gameboard.domain.user.repository.UserRepository;
import com.gamepan.gameboard.global.exception.BusinessException;
import com.gamepan.gameboard.global.exception.ErrorCode;
import com.gamepan.gameboard.global.help.AuthorizationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


// 사용자 관련 비즈니스 로직 처리를 위한 서비스
@Service
@RequiredArgsConstructor //userRepository 의존 주입 생성자 자동 생성
@Transactional // 데이터베이스 관련 작업시 transaction으로 묶어서 작업
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationService authorizationService;

    // Create - 유저 객체 만들기
    public User createUser(UserCreateRequestDto dto, Role role) {
        // 유저 객체 생성전 중복 검사 (username, email)
        if (userRepository.existsByUsernameAndIsDeletedFalse(dto.getUsername())){
            throw new DuplicateUsernameException();
        }
        if (userRepository.existsByEmailAndIsDeletedFalse(dto.getEmail())){
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
        return userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    // 전체 유저 조회
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //Read - 사용자 모두 가져오기
    public List<User> getAllActiveUsers() {
        return userRepository.findAllByIsDeletedFalse();
    }

    //Update - 사용자 정보 바꾸기(password, nickname 변경)
    /*public User updateUser(Long id, UserUpdateRequestDto dto) {
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
    }*/

    // 닉네임 변경
    @Transactional
    public void updateNickname(Long userId, String nickname) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        user.updateNickname(nickname); // 엔티티에 세터대신 도메인 메서드 권장
        // 영속 상태이므로 flush 시점에 자동 업데이트
    }

    // 비밀번호 변경
    @Transactional
    public void updatePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        // 비밀번호 정책 검증(길이/문자조합 등) 필요 시 추가
        user.updatePassword(passwordEncoder.encode(newPassword));
    }

    //  삭제 (Soft Delete 적용)
    public void softDeleteUser(Long id) {
        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        user.softDelete(); // BaseEntity의 softDelete() 메서드 호출
        userRepository.save(user);
    }

    //  복구
    public void restoreUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        user.restore(); // BaseEntity의 restore() 메서드 호출
        userRepository.save(user);
    }
}
