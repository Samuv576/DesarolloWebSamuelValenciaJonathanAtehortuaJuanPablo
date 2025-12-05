package com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityMembershipRepository extends JpaRepository<CommunityMembershipEntity, Long> {
    List<CommunityMembershipEntity> findByGroupId(Long groupId);
    boolean existsByGroupIdAndUsername(Long groupId, String username);
}
