package parcial2.backend.infraestructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "fcm_tokens",
        uniqueConstraints = @UniqueConstraint(columnNames = "token"))
public class FcmTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false, length = 500, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email", nullable = false)
    private UserEntity user;

    public FcmTokenEntity() {}

    public FcmTokenEntity(String token, UserEntity user) {
        this.token = token;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }
}

