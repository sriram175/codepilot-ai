package com.sriram.ai.codepilot_ai.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "repositories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Repository {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String url;
    private LocalDateTime createdAt;
}
