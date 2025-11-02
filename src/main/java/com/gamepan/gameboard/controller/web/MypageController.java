package com.gamepan.gameboard.controller.web;

import com.gamepan.gameboard.domain.comment.entity.Comment;
import com.gamepan.gameboard.domain.comment.service.CommentService;
import com.gamepan.gameboard.domain.like.service.LikeService;
import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.post.service.PostService;
import com.gamepan.gameboard.domain.user.dto.NicknameUpdateRequest;
import com.gamepan.gameboard.domain.user.dto.PasswordUpdateRequest;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class MypageController {
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;

    @GetMapping("/mypage")
    public String mypage(@AuthenticationPrincipal(expression = "user") User currentUser,
                         @RequestParam(defaultValue = "posts") String tab,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size,
                         Model model) {

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("activeTab", tab);

        PageRequest pageable = PageRequest.of(page, size);

        switch (tab) {
            case "comments" -> {
                Page<Post> commentedPosts = commentService.findCommentedPostsByUser(currentUser.getId(), pageable);
                model.addAttribute("commentedPosts", commentedPosts);
            }
            case "likes" -> {
                Page<Post> likedPosts = likeService.findLikedPostsPage(currentUser.getId(), pageable);
                model.addAttribute("likedPosts", likedPosts);
            }
            default -> { // posts
                Page<Post> myPosts = postService.findPageByAuthor(currentUser.getId(), pageable);
                model.addAttribute("posts", myPosts);
            }
        }
        return "mypage/index"; // templates/mypage/index.html
    }
    // 회원 정보 관리 페이지
    @GetMapping("/mypage/profile")
    public String profile(@AuthenticationPrincipal(expression = "user") User currentUser, Model model) {
        model.addAttribute("currentUser", currentUser);
        if (!model.containsAttribute("passwordForm")) {
            model.addAttribute("passwordForm", new PasswordUpdateRequest());
        }
        return "mypage/profile"; // templates/mypage/profile.html
    }

    // 닉네임 변경
    @PostMapping("/mypage/profile/nickname")
    public String updateNickname(@AuthenticationPrincipal(expression = "user") User currentUser,
                                 @Valid NicknameUpdateRequest req,
                                 BindingResult bindingResult,
                                 RedirectAttributes ra,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.nicknameForm", bindingResult);
            ra.addFlashAttribute("nicknameForm", req);
            return "redirect:/mypage/profile";
        }

        userService.updateNickname(currentUser.getId(), req.getNickname());

        // 보안 컨텍스트 포함 전체 로그아웃 처리
        new SecurityContextLogoutHandler().logout(request, response, null);

        ra.addFlashAttribute("logoutReason", "닉네임 변경으로 인해 자동 로그아웃되었습니다.");
        return "redirect:/login?reason=success";
    }

    @PostMapping("/mypage/profile/password")
    public String updatePassword(@AuthenticationPrincipal(expression = "user") User currentUser,
                                 @Valid @ModelAttribute("passwordForm") PasswordUpdateRequest req,
                                 BindingResult bindingResult,
                                 RedirectAttributes ra,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        // 1) Bean Validation 실패
        if (bindingResult.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.passwordForm", bindingResult);
            ra.addFlashAttribute("passwordForm", req);
            return "redirect:/mypage/profile";
        }

        // 2) 새 비밀번호 확인
        if (!req.getNewPassword().equals(req.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "password.mismatch", "비밀번호 확인이 일치하지 않습니다.");
            ra.addFlashAttribute("org.springframework.validation.BindingResult.passwordForm", bindingResult);
            ra.addFlashAttribute("passwordForm", req);
            return "redirect:/mypage/profile";
        }

        // 3) 서비스 실행(현재 비번 검증 포함)
        userService.updatePassword(currentUser.getId(), req.getCurrentPassword(), req.getNewPassword());

        // 4) 현재 세션 즉시 로그아웃(remember-me 쿠키도 같이 제거)
        new SecurityContextLogoutHandler().logout(request, response, null);

        // 5) 로그인 화면에서 안내 문구 노출
        return "redirect:/login?passwordChanged";
    }
}
