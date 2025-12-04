package com.Patinaje.V1.application.port.out;

import java.util.Set;

import com.Patinaje.V1.domain.model.Inscripcion;

public interface InscripcionRepository {
    boolean existsByClaseIdAndEstudianteId(Long claseId, Long estudianteId);
    Set<Long> findEstudiantesIdsByClase(Long claseId);
    Inscripcion save(Inscripcion inscripcion);
}
