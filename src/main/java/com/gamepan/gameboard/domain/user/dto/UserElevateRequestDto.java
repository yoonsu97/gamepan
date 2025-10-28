package com.gamepan.gameboard.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserElevateRequestDto {
    @NotBlank(message = "Admin 초대 코드를 입력하세요.")
    String inviteCode;
}
