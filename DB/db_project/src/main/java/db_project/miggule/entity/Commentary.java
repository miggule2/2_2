package db_project.miggule.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Commentary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer comment_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_account_id", nullable=false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="player_id", nullable=false)
    private Player player;

    @Column(columnDefinition = "TEXT", nullable=false)
    private String comment;

    @Column(nullable = false)
    private LocalDateTime created_at;

    public Commentary(){}

    public Commentary(User user, Player player, String comment){
        this.user = user;
        this.player = player;
        this.comment = comment;
        this.created_at = LocalDateTime.now();
    }

    public Integer getComment_id() {
        return comment_id;
    }

    public void setComment_id(Integer comment_id) {
        this.comment_id = comment_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
