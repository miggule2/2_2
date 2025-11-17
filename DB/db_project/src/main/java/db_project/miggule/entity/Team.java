package db_project.miggule.entity;

import jakarta.persistence.*;

@Entity
@Table(name="Teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // SERIAL 역할
    private Integer teamId;

    @Column(nullable = false, unique = true, length = 20) // domain
    private String teamName;

    public Team(){
    }

    // 초기화를 위한 생성자
    public Team(String team_name){
        this.teamName = team_name;
    }

    //
    // getter and setter
    //
    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }
}
