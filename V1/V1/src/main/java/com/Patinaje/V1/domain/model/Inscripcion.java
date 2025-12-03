package com.Patinaje.V1.domain.model;

import java.time.LocalDateTime;

import com.Patinaje.V1.shared.exception.DomainException;

public class Inscripcion {
    private final Long id;
    private final Long estudianteId;
    private final Long claseId;
    private final LocalDateTime fecha;

    public Inscripcion(Long id, Long estudianteId, Long claseId, LocalDateTime fecha) {
        if (estudianteId == null || claseId == null) {
            throw new DomainException("Inscripcion requiere estudiante y clase");
        }
        this.id = id;
        this.estudianteId = estudianteId;
        this.claseId = claseId;
        this.fecha = fecha == null ? LocalDateTime.now() : fecha;
    }

    public Long getId() {
        return id;
    }

    public Long getEstudianteId() {
        return estudianteId;
    }

    public Long getClaseId() {
        return claseId;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
