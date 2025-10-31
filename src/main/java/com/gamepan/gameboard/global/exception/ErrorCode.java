package com.gamepan.gameboard.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // === Global ===
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류가 발생했습니다."),
    INVALID_REQUEST(400, "잘못된 요청입니다."),

    // === Signup ===
    USER_EMAIL_DUPLICATE(409, "이미 존재하는 이메일입니다."),
    USER_USERNAME_DUPLICATE(409, "이미 존재하는 아이디입니다."),

    // === User ===
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    USER_DUPLICATE(409, "이미 존재하는 사용자 이름 또는 이메일입니다."),
    USER_FORBIDDEN(403, "사용자에 대한 권한이 없습니다."),
    PASSWORD_NOT_MATCH(400, "비밀번호가 일치하지 않습니다."),

    // === Board ===
    BOARD_NOT_FOUND(404, "게시판을 찾을 수 없습니다."),
    BOARD_FORBIDDEN(403, "게시판에 대한 권한이 없습니다."),
    BOARD_DUPLICATE(409, "이미 존재하는 게시판 코드입니다."),

    // === Post ===
    POST_NOT_FOUND(404, "게시글을 찾을 수 없습니다."),
    POST_FORBIDDEN(403, "게시글에 대한 권한이 없습니다."),

    // === Comment ===
    COMMENT_NOT_FOUND(404, "댓글을 찾을 수 없습니다."),
    COMMENT_FORBIDDEN(403, "댓글에 대한 권한이 없습니다."),

    // === Report ===
    REPORT_NOT_FOUND(404, "신고를 찾을 수 없습니다."),
    REPORT_FORBIDDEN(403, "신고에 대한 권한이 없습니다."),
    REPORT_POST_DUPLICATE(409, "이미 신고한 게시글입니다.");


    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}

