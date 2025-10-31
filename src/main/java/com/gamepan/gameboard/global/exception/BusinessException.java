package com.gamepan.gameboard.global.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        // 부모 클래스(RuntimeException)의 생성자를 호출하여 예외 메시지를 설정합니다.
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

