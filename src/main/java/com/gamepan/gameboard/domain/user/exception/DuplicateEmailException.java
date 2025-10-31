package com.gamepan.gameboard.domain.user.exception;

import com.gamepan.gameboard.global.exception.BusinessException;
import com.gamepan.gameboard.global.exception.ErrorCode;

public class DuplicateEmailException extends BusinessException {

    public DuplicateEmailException() {
        super(ErrorCode.USER_EMAIL_DUPLICATE);
    }
}