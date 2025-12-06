package com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventJpaRepository extends JpaRepository<EventEntity, Long> {
    List<EventEntity> findByFechaGreaterThanEqualOrderByFechaAsc(LocalDate fecha);
}
