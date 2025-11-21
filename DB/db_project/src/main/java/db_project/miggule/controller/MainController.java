// MainController.java
package db_project.miggule.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;

@Controller
public class MainController {
    // 홈화면
    @GetMapping("/")
    public String index(Model model, Principal principal){
        // Principal 객체 존재 여부를 통해 로그인 상태 확인
        boolean isAuthenticated = (principal != null);

        model.addAttribute("isAuthenticated", isAuthenticated);

        // 로그인이 됐을 경우 홈화면에 유저 이름을 띄우기 위함.
        if(isAuthenticated){
            model.addAttribute("username", principal.getName());
        }

        return "index"; // index.html 뷰 반환
    }
}