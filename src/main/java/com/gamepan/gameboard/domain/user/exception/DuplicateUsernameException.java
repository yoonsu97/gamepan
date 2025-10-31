package com.gamepan.gameboard.domain.user.exception;

import com.gamepan.gameboard.global.exception.BusinessException;
import com.gamepan.gameboard.global.exception.ErrorCode;

public class DuplicateUsernameException extends BusinessException {
    public DuplicateUsernameException() {
        super(ErrorCode.USER_USERNAME_DUPLICATE);
    }

}