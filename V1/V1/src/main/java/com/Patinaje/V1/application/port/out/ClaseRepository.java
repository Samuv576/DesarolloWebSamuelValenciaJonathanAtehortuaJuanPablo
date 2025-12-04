package com.Patinaje.V1.application.port.out;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import com.Patinaje.V1.domain.model.Clase;
import com.Patinaje.V1.domain.model.Nivel;

public interface ClaseRepository {
    Optional<Clase> findById(Long id);
    List<Clase> findByInstructorAndDia(Long instructorId, DayOfWeek dia);
    int countInscritos(Long claseId);
    List<Clase> findByNivel(Nivel nivel);
    List<Clase> findAll();
    Clase save(Clase clase);
}
