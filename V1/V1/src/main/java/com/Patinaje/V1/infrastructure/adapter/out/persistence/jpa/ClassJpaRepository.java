package com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Patinaje.V1.domain.model.Nivel;

public interface ClassJpaRepository extends JpaRepository<ClassEntity, Long> {
    List<ClassEntity> findByInstructorId(Long instructorId);
    List<ClassEntity> findByDiasSemanaContainingIgnoreCase(String dia);
    List<ClassEntity> findByNivel(Nivel nivel);
}
