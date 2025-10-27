package com.gamepan.gameboard.domain.user.exception;

//회원가입 시 email이 이미 존재하는 경우의 에러
public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException() { super("이미 사용 중인 이메일입니다."); }
}