package com.Patinaje.V1.domain.model;

import com.Patinaje.V1.shared.exception.DomainException;

public class Estudiante {
    private final Persona persona;
    private final Nivel nivel;
    private final String contactoEmergencia;

    public Estudiante(Persona persona, Nivel nivel, String contactoEmergencia) {
        if (persona == null) {
            throw new DomainException("El estudiante requiere persona");
        }
        this.persona = persona;
        this.nivel = nivel == null ? Nivel.BASICO : nivel;
        if (contactoEmergencia == null || contactoEmergencia.trim().isEmpty()) {
            throw new DomainException("El contacto de emergencia es obligatorio");
        }
        this.contactoEmergencia = contactoEmergencia.trim();
    }

    public Persona getPersona() {
        return persona;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public String getContactoEmergencia() {
        return contactoEmergencia;
    }
}
