package com.gamepan.gameboard.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NicknameUpdateRequest {
    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Size(max = 30, message = "닉네임은 30자 이내로 입력해 주세요.")
    private String nickname;
}
