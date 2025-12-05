package com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "community_messages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private CommunityGroupEntity group;

    @Column(nullable = false, length = 2000)
    private String contenido;

    @Column(nullable = false)
    private String autor;

    @Column(nullable = false)
    private String rolAutor;

    @Column(nullable = false)
    private LocalDateTime creadoEn;
}
