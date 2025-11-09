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
}