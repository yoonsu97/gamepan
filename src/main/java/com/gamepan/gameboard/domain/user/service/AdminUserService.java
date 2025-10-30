package com.gamepan.gameboard.domain.user.service;

import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminUserService {

    private final UserRepository userRepository;

    // 삭제되지 않은 회원 전체 조회
    public List<User> getAllActiveUsers() {
        return userRepository.findAllByIsDeletedFalse();
    }

    // 삭제된 회원 전체 조회
    public List<User> getAllDeletedUsers() {
        return userRepository.findAllByIsDeletedTrue();
    }

    public void softDeleteUser(Long id) {
        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 사용자가 존재하지 않습니다."));

        user.softDelete(); // BaseEntity의 softDelete() 메서드 호출
        userRepository.save(user);
    }

    public int deleteUsers(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return 0;

        int count = 0;
        for (Long id : ids) {
            try {
                softDeleteUser(id);
                count++;
            } catch (IllegalArgumentException e) {
                // 이미 삭제된 경우 등은 무시
            }
        }
        return count;
    }

    public void restoreUser(Long id) {
        User user = userRepository.findByIdAndIsDeletedTrue(id)
                .orElseThrow(() -> new IllegalArgumentException("복구할 사용자가 존재하지 않습니다."));
        user.restore(); // BaseEntity의 복구 메서드
        userRepository.save(user);
    }

    public int restoreUsers(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return 0;
        int count = 0;
        for (Long id : ids) {
            try {
                restoreUser(id);
                count++;
            } catch (IllegalArgumentException ignored) {}
        }
        return count;
    }
}