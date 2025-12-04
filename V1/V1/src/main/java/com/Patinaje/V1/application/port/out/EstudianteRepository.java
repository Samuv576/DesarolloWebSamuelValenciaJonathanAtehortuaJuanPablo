package com.Patinaje.V1.application.port.out;

import java.util.Optional;

import com.Patinaje.V1.domain.model.Estudiante;

public interface EstudianteRepository {
    Optional<Estudiante> findById(Long id);
    Optional<Estudiante> findByIdentificacion(String identificacion);
    Estudiante save(Estudiante estudiante);
}
