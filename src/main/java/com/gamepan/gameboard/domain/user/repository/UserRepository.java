package com.gamepan.gameboard.domain.user.repository;

import com.gamepan.gameboard.domain.user.entity.Role;
import com.gamepan.gameboard.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 사용자 JPARepository
public interface UserRepository extends JpaRepository<User, Long> {
    public Boolean existsByUsername(String username); // 해당 username이 존재하는지 확인
    public Boolean existsByEmail(String email);       // 해당 email이 존재하는지 확인
    public Boolean existsByRole(Role role);
    public Optional<User> findByUsername(String username);      // username을 통해 user를 가져옴
}
