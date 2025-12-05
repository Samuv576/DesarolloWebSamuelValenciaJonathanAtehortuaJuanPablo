package com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceJpaRepository extends JpaRepository<AttendanceEntity, Long> {
    List<AttendanceEntity> findByClaseIdAndFecha(Long claseId, LocalDate fecha);
    void deleteByClaseIdAndFecha(Long claseId, LocalDate fecha);
}
