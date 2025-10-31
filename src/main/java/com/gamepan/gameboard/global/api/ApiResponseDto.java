package com.gamepan.gameboard.global.api;

import lombok.Getter;

@Getter
public class ApiResponseDto<T> {

    /*응답 결과 메시지*/
    private final String message;

    /*실제 응답 데이터*/
    private final T data;

    private ApiResponseDto(String message, T data) {
        this.message = message;
        this.data = data;
    }

    /**
     * 성공 응답을 생성합니다.
     * @param message 성공 메시지
     * @param data 응답 데이터
     */
    public static <T> ApiResponseDto<T> success(String message, T data) {
        return new ApiResponseDto<>(message, data);
    }

    /**
     * 실패 응답을 생성합니다. (데이터는 null)
     * @param message 실패 메시지
     */
    public static <T> ApiResponseDto<T> fail(String message) {
        return new ApiResponseDto<>(message, null);
    }
}

