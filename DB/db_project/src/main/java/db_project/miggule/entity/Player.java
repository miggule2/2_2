package db_project.miggule.entity;

import jakarta.persistence.*;

@Entity
@Table(name="players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // SERIAL 역할
    private Integer playerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="teamId", nullable = false)
    private Team team;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userAccount", unique = true)
    private User user;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 10)
    private String position;

    @Column(nullable = false)
    private Integer salary=0;

    @Column(nullable = false)
    private boolean isAlien = false;

    // 이미지를 위한 필드(속성)
    @Column
    private String imageUrl;

    public Player(){
    }

    // 초기화를 위한 생성자
    public Player(Team team, String name, String position, Integer salary, String imageUrl, boolean isAlien){
        this.team = team;
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.isAlien = isAlien;
        this.imageUrl = imageUrl;
    }

    //
    // getter and setter
    //
    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
