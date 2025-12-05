package com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "community_memberships")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityMembershipEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private CommunityGroupEntity group;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String rol;
}
