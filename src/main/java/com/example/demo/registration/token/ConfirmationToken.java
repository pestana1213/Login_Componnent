package com.example.demo.registration.token;

import com.example.demo.appuser.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private UUID id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime generatedAt;
    @Column(nullable = false)
    private LocalDateTime expiredAt;
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    private AppUser appUser; //1 user pode ter varios tokens

    public ConfirmationToken(String token,
                             LocalDateTime generatedAt,
                             LocalDateTime expiredAt,
                             AppUser appUser) {
        this.token = token;
        this.generatedAt = generatedAt;
        this.expiredAt = expiredAt;
        this.appUser = appUser;
    }
}
