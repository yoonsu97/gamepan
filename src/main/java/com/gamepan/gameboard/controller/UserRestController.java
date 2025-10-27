package com.gamepan.gameboard.controller;

import com.gamepan.gameboard.domain.user.dto.UserCreateRequestDto;
import com.gamepan.gameboard.domain.user.dto.UserResponseDto;
import com.gamepan.gameboard.domain.user.dto.UserUpdateRequestDto;
import com.gamepan.gameboard.domain.user.entity.Role;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    /** ✅ 모든 사용자 조회 (GET /api/users) */
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers()
                .stream()
                .map(UserResponseDto::from)
                .toList();
        return ResponseEntity.ok(users);
    }

    /** ✅ 특정 사용자 조회 (GET /api/users/{id}) */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(UserResponseDto.from(user));
    }

    /** ✅ 사용자 등록 (POST /api/users) */
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserCreateRequestDto request) {
        User user = userService.createUser(request, Role.USER);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDto.from(user));
    }

    /** ✅ 사용자 수정 (PUT /api/users/{id}) */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequestDto request) {
        User updated = userService.updateUser(id, request);
        return ResponseEntity.ok(UserResponseDto.from(updated));
    }

    /** ✅ 사용자 삭제 (DELETE /api/users/{id}) */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

