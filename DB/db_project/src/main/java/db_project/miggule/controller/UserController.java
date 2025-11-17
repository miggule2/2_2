package db_project.miggule.controller;

import db_project.miggule.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입 폼을 보여주는 페이지
    @GetMapping("/signup")
    public String showSignupForm() {
        return "signup"; // signup.html파일 찾기
    }

    // 회원가입 폼 데이터를 받아서 처리
    @PostMapping("/signup")
    public String processSignup(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email
    ) {
        // 회원가입 로직 실행
        userService.signup(username, password, email);

        // 회원가입 성공 시, 로그인 페이지나 메인 페이지로 리다이렉트
        return "redirect:/"; // (루트 페이지로 보냄)
    }
}