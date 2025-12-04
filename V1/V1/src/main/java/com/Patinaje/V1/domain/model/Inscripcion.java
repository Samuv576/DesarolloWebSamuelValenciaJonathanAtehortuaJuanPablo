package com.Patinaje.V1.domain.model;

import java.time.LocalDateTime;

import com.Patinaje.V1.shared.exception.DomainException;

public class Inscripcion {
    private final Long id;
    private final Long estudianteId;
    private final Long claseId;
    private final LocalDateTime fecha;
    private final EstadoInscripcion estado;

    public Inscripcion(Long id, Long estudianteId, Long claseId, LocalDateTime fecha, EstadoInscripcion estado) {
        if (estudianteId == null || claseId == null) {
            throw new DomainException("Inscripcion requiere estudiante y clase");
        }
        this.id = id;
        this.estudianteId = estudianteId;
        this.claseId = claseId;
        this.fecha = fecha == null ? LocalDateTime.now() : fecha;
        this.estado = estado == null ? EstadoInscripcion.ACTIVA : estado;
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

    public EstadoInscripcion getEstado() {
        return estado;
    }
}
