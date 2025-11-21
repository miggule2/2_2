package db_project.miggule.service;

import db_project.miggule.entity.User;
import db_project.miggule.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder; // 비밀번호 암호화 클래스
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService implements UserDetailsService { // UserDetailsService 구현
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 생성자
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 로그인 로직
    @Transactional
    public void signup(String username, String rawPassword, String email) {
        String hashedPassword = passwordEncoder.encode(rawPassword); // 비밀번호 암호화
        User newUser = new User(username, hashedPassword, email); // 새 유저 객체 생성
        userRepository.save(newUser); // DB에 저장 (Repository 사용)
    }

    // Spring Security 인증을 위한 필수 메서드
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // DB에서 유저 정보 조회
        // User에서 UserDetails를 구현해뒀기 때문에 자동으로 반환값이 UserDetails가 됨.
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));
    }
}