/*
package com.gamepan.gameboard.domain.user.controller;

import com.gamepan.gameboard.domain.user.dto.UserElevateRequestDto;
import com.gamepan.gameboard.domain.user.dto.UserResponseDto;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.domain.user.service.UserService;
import com.gamepan.gameboard.global.api.ApiResponse;
import com.gamepan.gameboard.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    */
/** ✅ 모든 사용자 조회 (GET /api/users) *//*

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllActiveUsers()
                .stream()
                .map(UserResponseDto::from)
                .toList();
        return ResponseEntity.ok(users);
    }

    */
/** ✅ 특정 사용자 조회 (GET /api/users/{id}) *//*

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(UserResponseDto.from(user));
    }

    */
/** ✅ 사용자 등록 (POST /api/users) *//*
*/
/*
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserCreateRequestDto request) {
        User user = userService.createUser(request, Role.USER);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDto.from(user));
    }*//*


    */
/** ✅ 사용자 수정 (PUT /api/users/{id}) *//*

    */
/*@PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequestDto request) {
        User updated = userService.updateUser(id, request);
        return ResponseEntity.ok(UserResponseDto.from(updated));
    }*//*


    */
/** ✅ 사용자 삭제 (DELETE /api/users/{id}) *//*

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteUser(@PathVariable Long id) {
        userService.softDeleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ 사용자 복구
    @PutMapping("/{id}/restore")
    public ResponseEntity<String> restoreUser(@PathVariable Long id) {
        userService.restoreUser(id);
        return ResponseEntity.ok("사용자가 복구되었습니다.");
    }

    // 일반 유저에서 인증 코드를 통한 관리자 승급
    // fixme: 초대 코드로 현재 권한을 받으면 즉시 권한이 부여되는게 아니라 재로그인시 권한이 부여됨.
    @PostMapping("/invite")
    public ResponseEntity<ApiResponse<Void>> invite(@Valid @RequestBody UserElevateRequestDto dto,
                                                    @AuthenticationPrincipal CustomUserDetails principal) {
        userService.inviteToAdmin(principal.getUser().getId(), dto.getInviteCode());
        return ResponseEntity.ok(ApiResponse.ok(null, "관리자로 승격되었습니다."));
    }

}

*/
