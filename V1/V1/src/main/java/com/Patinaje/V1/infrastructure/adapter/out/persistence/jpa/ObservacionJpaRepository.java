package com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObservacionJpaRepository extends JpaRepository<ObservacionEntity, Long> {
    List<ObservacionEntity> findByEstudianteIdOrderByFechaDesc(Long estudianteId);
}
