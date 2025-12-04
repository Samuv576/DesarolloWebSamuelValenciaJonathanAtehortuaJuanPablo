package com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentJpaRepository extends JpaRepository<EnrollmentEntity, Long> {
    List<EnrollmentEntity> findByEstudianteId(Long estudianteId);
    List<EnrollmentEntity> findByClaseId(Long claseId);
    boolean existsByEstudianteIdAndClaseId(Long estudianteId, Long claseId);
    long countByClaseId(Long claseId);
}
