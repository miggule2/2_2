package db_project.miggule.service;

import db_project.miggule.entity.User;
import db_project.miggule.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder; // 비밀번호 암호화 클래스
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 생성자
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 로그인 로직
    public void signup(String username, String rawPassword, String email) {
        String hashedPassword = passwordEncoder.encode(rawPassword); // 비밀번호 암호화
        User newUser = new User(username, hashedPassword, email); // 새 유저 객체 생성
        userRepository.save(newUser); // DB에 저장 (Repository 사용)
    }
}