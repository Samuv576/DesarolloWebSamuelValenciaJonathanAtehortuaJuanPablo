package com.Patinaje.V1.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.Patinaje.V1.shared.exception.DomainException;

public class Pago {
    private final Long id;
    private final Long estudianteId;
    private final BigDecimal monto;
    private final LocalDate fecha;

    public Pago(Long id, Long estudianteId, BigDecimal monto, LocalDate fecha) {
        if (estudianteId == null) {
            throw new DomainException("El pago requiere estudiante");
        }
        if (monto == null || monto.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("El monto de pago debe ser >= 0");
        }
        this.id = id;
        this.estudianteId = estudianteId;
        this.monto = monto;
        this.fecha = fecha == null ? LocalDate.now() : fecha;
    }

    public boolean esMoroso(LocalDate fechaReferencia) {
        return fecha.isBefore(fechaReferencia.minusDays(30));
    }

    public Long getId() {
        return id;
    }

    public Long getEstudianteId() {
        return estudianteId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public LocalDate getFecha() {
        return fecha;
    }
}
