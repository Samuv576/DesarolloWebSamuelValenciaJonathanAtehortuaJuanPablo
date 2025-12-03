package com.Patinaje.V1.domain.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.Patinaje.V1.shared.exception.DomainException;

public class Clase {
    private final Long id;
    private final String nombre;
    private final Nivel nivel;
    private final DayOfWeek dia;
    private final LocalTime horaInicio;
    private final LocalTime horaFin;
    private final int cupo;
    private final Long instructorId;

    public Clase(Long id, String nombre, Nivel nivel, DayOfWeek dia, LocalTime horaInicio, LocalTime horaFin, int cupo, Long instructorId) {
        this.id = id;
        this.nombre = requireNonBlank(nombre, "El nombre de la clase es obligatorio");
        this.nivel = nivel == null ? Nivel.BASICO : nivel;
        if (dia == null) {
            throw new DomainException("El dia de la clase es obligatorio");
        }
        this.dia = dia;
        if (horaInicio == null || horaFin == null || !horaFin.isAfter(horaInicio)) {
            throw new DomainException("La hora fin debe ser posterior a la hora inicio");
        }
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        if (cupo < 5 || cupo > 30) {
            throw new DomainException("El cupo debe estar entre 5 y 30");
        }
        this.cupo = cupo;
        if (instructorId == null) {
            throw new DomainException("La clase requiere instructor asignado");
        }
        this.instructorId = instructorId;
    }

    private String requireNonBlank(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new DomainException(message);
        }
        return value.trim();
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public DayOfWeek getDia() {
        return dia;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public int getCupo() {
        return cupo;
    }

    public Long getInstructorId() {
        return instructorId;
    }
}
