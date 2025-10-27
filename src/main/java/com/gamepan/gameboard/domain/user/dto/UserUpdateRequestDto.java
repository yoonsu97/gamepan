package com.gamepan.gameboard.domain.user.dto;

import lombok.Getter;

@Getter
public class UserUpdateRequestDto {
    private String password;
    private String nickname;
}
