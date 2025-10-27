package com.gamepan.gameboard.global.advice;

import com.gamepan.gameboard.domain.user.dto.SignupRequestDto;
import com.gamepan.gameboard.domain.user.exception.DuplicateEmailException;
import com.gamepan.gameboard.domain.user.exception.DuplicateUsernameException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(annotations = Controller.class)
public class GlobalMvcExceptionHandler {
    @ExceptionHandler(DuplicateUsernameException.class)
    public String handleDuplicateUsername(
            DuplicateUsernameException e,
            @ModelAttribute("form") SignupRequestDto form, // 요청 파라미터를 다시 바인딩하여 폼 값 유지
            BindingResult bindingResult              // 폼 옆에 필드 에러 노출
    ) {
        bindingResult.rejectValue("username", "Duplicate", e.getMessage());
        return "auth/signup";
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public String handleDuplicateEmail(
            DuplicateEmailException e,
            @ModelAttribute("form") SignupRequestDto form,
            BindingResult bindingResult
    ) {
        bindingResult.rejectValue("email", "Duplicate", e.getMessage());
        return "auth/signup";
    }
}
