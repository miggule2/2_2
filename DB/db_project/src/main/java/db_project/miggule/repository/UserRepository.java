package db_project.miggule.repository;

import db_project.miggule.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<엔티티 클래스, Pk 타입>
public interface UserRepository extends JpaRepository<User, Integer> {
    // jpa에서 기본 CRUD 제공
}