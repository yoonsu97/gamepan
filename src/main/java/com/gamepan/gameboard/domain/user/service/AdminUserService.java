package com.gamepan.gameboard.domain.user.service;

import com.gamepan.gameboard.domain.user.entity.Role;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.domain.user.repository.UserRepository;
import com.gamepan.gameboard.global.exception.BusinessException;
import com.gamepan.gameboard.global.exception.ErrorCode;
import com.gamepan.gameboard.global.help.AuthorizationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminUserService {

    private final UserRepository userRepository;
    private final AuthorizationService authorizationService;

    @Value("${app.invite-code:}")   // application-*.yml 에 설정해둔 초대코드
    private String adminInviteCode;

    // 삭제되지 않은 회원 전체 조회
    public List<User> getAllActiveUsers() {
        return userRepository.findAllByIsDeletedFalse();
    }

    // 삭제된 회원 전체 조회
    public List<User> getAllDeletedUsers() {
        return userRepository.findAllByIsDeletedTrue();
    }

    public void softDeleteUser(Long id, User currentUser) {
        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        authorizationService.AdminHasUserPermission(currentUser,ErrorCode.USER_FORBIDDEN);

        user.softDelete(); // BaseEntity의 softDelete() 메서드 호출
        userRepository.save(user);
    }

    public int deleteUsers(List<Long> ids, User currentUser) {
        if (ids == null || ids.isEmpty()) return 0;

        int count = 0;
        for (Long id : ids) {
            try {
                softDeleteUser(id, currentUser);
                count++;
            } catch (IllegalArgumentException e) {
                // 이미 삭제된 경우 등은 무시
            }
        }
        return count;
    }

    public void restoreUser(Long id, User currentUser) {
        User user = userRepository.findByIdAndIsDeletedTrue(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        authorizationService.AdminHasUserPermission(currentUser, ErrorCode.USER_FORBIDDEN);

        user.restore(); // BaseEntity의 복구 메서드
        userRepository.save(user);
    }

    public int restoreUsers(List<Long> ids, User currentUser) {
        if (ids == null || ids.isEmpty()) return 0;
        int count = 0;
        for (Long id : ids) {
            try {
                restoreUser(id, currentUser);
                count++;
            } catch (IllegalArgumentException ignored) {}
        }
        return count;
    }

    // 권한이 존재하는지 확인 (admin 권한 부여에 사용)
    public boolean existsByRole(Role role) {
        return userRepository.existsByRoleAndIsDeletedFalse(role);
    }

    @Transactional
    public void promoteToAdminByUsername(String username) {
        var user = userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
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
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setAdmin(); // 내부에서 role = ADMIN 으로 세팅되는 메서드
        // 변경감지로 업데이트 반영
    }
}