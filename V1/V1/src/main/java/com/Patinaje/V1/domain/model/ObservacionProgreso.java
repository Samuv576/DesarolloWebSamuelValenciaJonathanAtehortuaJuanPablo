package com.Patinaje.V1.domain.model;

import java.time.LocalDate;

import com.Patinaje.V1.shared.exception.DomainException;

public class ObservacionProgreso {
    private final Long id;
    private final Long estudianteId;
    private final Long instructorId;
    private final LocalDate fecha;
    private final int control;
    private final int velocidad;
    private final int equilibrio;
    private final int frenado;
    private final String nota;

    public ObservacionProgreso(Long id,
                               Long estudianteId,
                               Long instructorId,
                               LocalDate fecha,
                               int control,
                               int velocidad,
                               int equilibrio,
                               int frenado,
                               String nota) {
        if (estudianteId == null || instructorId == null) {
            throw new DomainException("Observacion requiere estudiante e instructor");
        }
        this.id = id;
        this.estudianteId = estudianteId;
        this.instructorId = instructorId;
        this.fecha = fecha == null ? LocalDate.now() : fecha;
        this.control = validarRango(control, "control");
        this.velocidad = validarRango(velocidad, "velocidad");
        this.equilibrio = validarRango(equilibrio, "equilibrio");
        this.frenado = validarRango(frenado, "frenado");
        this.nota = nota == null ? "" : nota.trim();
    }

    private int validarRango(int valor, String campo) {
        if (valor < 0 || valor > 10) {
            throw new DomainException("El campo " + campo + " debe estar entre 0 y 10");
        }
        return valor;
    }

    public Long getId() {
        return id;
    }

    public Long getEstudianteId() {
        return estudianteId;
    }

    public Long getInstructorId() {
        return instructorId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getControl() {
        return control;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public int getEquilibrio() {
        return equilibrio;
    }

    public int getFrenado() {
        return frenado;
    }

    public String getNota() {
        return nota;
    }
}
