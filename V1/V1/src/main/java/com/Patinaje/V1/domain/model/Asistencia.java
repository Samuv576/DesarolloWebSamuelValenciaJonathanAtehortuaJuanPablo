package com.Patinaje.V1.domain.model;

import java.time.LocalDate;

import com.Patinaje.V1.shared.exception.DomainException;

public class Asistencia {
    private final Long id;
    private final Long claseId;
    private final Long estudianteId;
    private final LocalDate fecha;
    private final boolean presente;

    public Asistencia(Long id, Long claseId, Long estudianteId, LocalDate fecha, boolean presente) {
        if (claseId == null || estudianteId == null) {
            throw new DomainException("La asistencia requiere clase y estudiante");
        }
        LocalDate hoy = LocalDate.now();
        LocalDate f = fecha == null ? hoy : fecha;
        if (!f.equals(hoy)) {
            throw new DomainException("La asistencia solo se registra el mismo dia");
        }
        this.id = id;
        this.claseId = claseId;
        this.estudianteId = estudianteId;
        this.fecha = f;
        this.presente = presente;
    }

    public Long getId() {
        return id;
    }

    public Long getClaseId() {
        return claseId;
    }

    public Long getEstudianteId() {
        return estudianteId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public boolean isPresente() {
        return presente;
    }
}
