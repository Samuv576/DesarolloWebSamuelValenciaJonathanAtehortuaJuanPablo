package com.Patinaje.V1.domain.model;

import com.Patinaje.V1.shared.exception.DomainException;

public class Instructor {
    private final Persona persona;
    private final String especialidad;

    public Instructor(Persona persona, String especialidad) {
        if (persona == null) {
            throw new DomainException("El instructor requiere persona");
        }
        this.persona = persona;
        this.especialidad = especialidad == null ? "" : especialidad.trim();
    }

    public Persona getPersona() {
        return persona;
    }

    public String getEspecialidad() {
        return especialidad;
    }
}
