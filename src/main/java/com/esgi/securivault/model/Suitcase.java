package com.esgi.securivault.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Entity
@Table(name = "suitcases")
@Data
public class Suitcase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String code;
    private Double latitude;
    private Double longitude;
    private String lightColor;
    private Integer buzzerVolume;
    private Boolean isLocked;
    private Boolean motionDetected;

    @ManyToMany
    @JoinTable(name = "user_suitcase",
            joinColumns = @JoinColumn(name = "suitcase_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users;
}