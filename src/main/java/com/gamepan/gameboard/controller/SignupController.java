package com.gamepan.gameboard.controller;

import com.gamepan.gameboard.domain.user.dto.SignupRequestDto;
import com.gamepan.gameboard.domain.user.entity.Role;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.domain.user.exception.DuplicateEmailException;
import com.gamepan.gameboard.domain.user.exception.DuplicateUsernameException;
import com.gamepan.gameboard.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SignupController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("form", new SignupRequestDto());
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String doSignup(@Valid @ModelAttribute("form") SignupRequestDto form,
                           BindingResult bindingResult,
                           Model model) {
        // 비밀번호와 비밀번호 확인이 다를 경우
        if (!form.getPassword().equals(form.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "Mismatch", "비밀번호가 일치하지 않습니다.");
        }
        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        try {
            User user = userService.createUser(form.toCreateDto(), Role.USER);
        } catch (DuplicateUsernameException e) {
            bindingResult.rejectValue("username", "Duplicate", e.getMessage());
            return "auth/signup";
        } catch (DuplicateEmailException e) {
            // 중복 아이디/이메일 등 비즈니스 예외를 IllegalStateException으로 던진 경우
            bindingResult.rejectValue("email", "Duplicate", e.getMessage());
            return "auth/signup";
        }

        // 회원가입 후 로그인 페이지로 이동 (알림 파라미터)
        return "redirect:/login?signup=success";
    }
}
