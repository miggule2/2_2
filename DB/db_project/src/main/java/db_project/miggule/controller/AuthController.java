package db_project.miggule.controller;

import db_project.miggule.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // GET /signup: 회원가입 페이지 반환
    @GetMapping("/signup")
    public String showSignupForm(){
        return "signup";
    }

    // POST /signup: 회원가입 요청을 처리
    @PostMapping("/signup")
    public String signup(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email){
        userService.signup(username, password, email);

        return "redirect:/login";
    }

    // GET /login: 로그인 페이지 반환
    @GetMapping("/login")
    public String showLoginForm(){
        return "login";
    }
}