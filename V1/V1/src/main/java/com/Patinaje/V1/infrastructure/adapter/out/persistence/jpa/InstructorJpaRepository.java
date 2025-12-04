package com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorJpaRepository extends JpaRepository<InstructorEntity, Long> {
    Optional<InstructorEntity> findByIdentificacion(String identificacion);
}
