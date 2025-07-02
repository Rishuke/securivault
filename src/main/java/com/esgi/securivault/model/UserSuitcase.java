package com.esgi.securivault.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_suitcase")
@Data
public class UserSuitcase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "suitcase_id")
    private Suitcase suitcase;

    private String accessLevel; // "OWNER", "USER"

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}