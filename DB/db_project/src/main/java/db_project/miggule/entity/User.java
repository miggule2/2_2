package db_project.miggule.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users") // DB의 'users' 테이블과 연결
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // SERIAL 역할
    @Column(name = "user_id")
    private Integer user_id;

    @Column(name = "username", nullable = false, unique = true, length = 20) // domain
    private String username;

    @Column(name = "hashed_password", nullable = false, length = 255)
    private String hashedPassword;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "points", nullable = false)
    private Integer points;

    @Column(name = "user_role", nullable = false, length = 10)
    private String userRole;

    // JPA 기본 생성자
    public User() {
    }

    // 회원가입용 생성자
    public User(String username, String hashedPassword, String email) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.email = email;
        this.points = 0; // 기본값
        this.userRole = "general"; // 기본값
    }

    // Getter, Setter... (필요시)
}