package com.gamepan.gameboard.domain.user.dto;

import com.gamepan.gameboard.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "from")
@Builder
public class UserResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private String email;


    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build();
    }
}