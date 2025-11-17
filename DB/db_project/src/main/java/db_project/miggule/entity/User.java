package db_project.miggule.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Users") // DB의 'users' 테이블과 연결
public class User {

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
    public User(String username, String hashedPassword, String email) {
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

    public String getUsername() {
        return username;
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
}