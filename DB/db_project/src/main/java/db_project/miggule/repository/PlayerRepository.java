package db_project.miggule.repository;

import db_project.miggule.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    // jpa에서 기본 CRUD 제공
}