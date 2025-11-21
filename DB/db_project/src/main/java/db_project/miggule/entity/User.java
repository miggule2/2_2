package db_project.miggule.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "Users") // DB의 'users' 테이블과 연결
public class User implements UserDetails {

    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // SERIAL 역할
    private Integer userId;

    @Column(nullable = false, unique = true, length = 20) // domain
    private String username;

    @Column(nullable = false, length = 255)
    private String hashedPassword;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Integer points;

    @Column(nullable = false, length = 10)
    private String userRole;

    // JPA 기본 생성자
    public User() {
    }

    // 초기화를 위한 생성자
    public User(String username, String hashedPassword, String email){
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.email = email;
        this.points = 0; // 기본값
        this.userRole = "general"; // 기본값
    }

    //
    // getter and setter
    //
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    // UserDetails 구현 메서드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // userRole 필드를 사용해 객체 권한 생성
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + this.userRole.toUpperCase())
        );
    }

    @Override
    public String getPassword() {
        return this.getHashedPassword();
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}