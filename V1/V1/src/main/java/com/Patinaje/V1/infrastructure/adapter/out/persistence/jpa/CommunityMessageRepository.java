package com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityMessageRepository extends JpaRepository<CommunityMessageEntity, Long> {
    List<CommunityMessageEntity> findByGroupIdOrderByCreadoEnAsc(Long groupId);
}
