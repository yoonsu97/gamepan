package com.gamepan.gameboard.domain.user.exception;

// 회원가입 시 사용자 아이디가 이미 존재할 경우의 에러
public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException() { super("이미 사용 중인 아이디입니다."); }
}