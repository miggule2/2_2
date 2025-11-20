package db_project.miggule.repository;

import db_project.miggule.entity.Commentary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentaryRepository extends JpaRepository<Commentary, Integer> {
    // 1. 팀별 코멘터리 목록 조회
    @Query("select c from Commentary c join c.player p join p.team t where t.teamId = :teamId order by c.player.name, c.created_at desc")
    List<Commentary> findByTeamId(@Param("teamId")Integer teamId);

    // 2. 선수 코멘터리 검색 기능 (여러 선수 검색 가능)
    @Query("select c from Commentary c join c.player p where p.name like %:player% order by c.player.name, c.created_at desc")
    List<Commentary> findByPlayerName(@Param("player")String player);


    // 4. 공식 코멘터리 판별 ( 배열[0] : 코멘토리 / 배역[1] : 코맨터리 작성자가 선수인지 판별
    // 작성 유저가 선수 본인의 유저 id와 동일할 경우 true를 반환
    @Query("SELECT c, CASE WHEN c.user.userId = p.user.userId THEN TRUE ELSE FALSE END " +
            "from Commentary c join c.player p "+
            "where p.playerId = :playerId " +
            "order by c.created_at desc")
    List<Object[]> findCommentaryAndOfficialStatus(@Param("playerId") Integer playerId);
}
