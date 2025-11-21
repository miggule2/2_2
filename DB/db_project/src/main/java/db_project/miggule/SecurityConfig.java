package db_project.miggule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // SpringSecurity 활성화
public class SecurityConfig {
    // 1. 비밀번호 암호화 방식 정의
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 다음의 암호화 알고리즘 사용
    }

    // 2. 보안 필터 체인 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // csrf 비활성화 (프로젝트의 간소화를 위해)
                .authorizeHttpRequests(authorize -> authorize
                        // 인증 없이 접근 허용할 경로 (로그인/회원가입/홈화면)
                        .requestMatchers(
                                "/signup",
                                "/css/**",
                                "/js/**",
                                "/login",
                                "/error",
                                "/"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                // 로그인 설정
                .formLogin(formLogin -> formLogin
                        .loginPage("/login") // 로그인 페이지 경로
                        .defaultSuccessUrl("/") // 로그인 성공시 경로
                        .failureUrl("/login?error") // 로그인 실패시 이동 페이지
                        .permitAll()
                )
                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/logout")// 로그아웃 경로
                        .logoutSuccessUrl("/") // 로그아웃 성공시 이동 경로
                        .invalidateHttpSession(true)
                );
        return http.build();
    }
}
