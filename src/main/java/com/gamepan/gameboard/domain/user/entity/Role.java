package com.gamepan.gameboard.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER", "일반 사용자"),

    ADMIN("ROLE_ADMIN", "관리자");

    private final String code;
    private final String description;
}