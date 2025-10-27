package com.gamepan.gameboard.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserCreateRequestDto {
    private String username;
    private String password;
    private String nickname;
    private String email;
}
