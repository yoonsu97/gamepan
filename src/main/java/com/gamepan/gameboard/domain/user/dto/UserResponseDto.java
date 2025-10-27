package com.gamepan.gameboard.domain.user.dto;

import com.gamepan.gameboard.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "from")
public class UserResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private String email;


    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getEmail()
        );
    }
}