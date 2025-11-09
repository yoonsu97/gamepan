package com.gamepan.gameboard.domain.user.entity;

import com.gamepan.gameboard.global.exception.BusinessException;
import com.gamepan.gameboard.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("U", "일반 사용자"),

    ADMIN("A", "관리자");

    private final String code;
    private final String description;

    public static Role fromCode(String code) {
        return Arrays.stream(Role.values())
                .filter(r -> r.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.ROLE_CODE_NOT_FOUND));
    }
}