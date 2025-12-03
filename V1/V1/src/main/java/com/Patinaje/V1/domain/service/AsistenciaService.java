package com.Patinaje.V1.domain.service;

import java.time.LocalDate;
import java.util.Set;

import com.Patinaje.V1.domain.model.Asistencia;
import com.Patinaje.V1.shared.exception.DomainException;

public class AsistenciaService {

    /**
     * Registra asistencia solo para inscritos y solo el mismo día.
     */
    public Asistencia registrarAsistencia(Long claseId, Long estudianteId, LocalDate fecha, boolean presente, Set<Long> inscritosClase) {
        if (inscritosClase == null || !inscritosClase.contains(estudianteId)) {
            throw new DomainException("No se puede registrar asistencia de un no inscrito");
        }
        return new Asistencia(null, claseId, estudianteId, fecha, presente);
    }
}
