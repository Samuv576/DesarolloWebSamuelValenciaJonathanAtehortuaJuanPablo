package com.Patinaje.V1.domain.service;

import java.time.LocalDate;
import java.util.List;

import com.Patinaje.V1.domain.model.Clase;
import com.Patinaje.V1.domain.model.Estudiante;
import com.Patinaje.V1.domain.model.Inscripcion;
import com.Patinaje.V1.domain.model.Pago;
import com.Patinaje.V1.shared.exception.DomainException;

public class InscripcionService {

    /**
     * Valida reglas de inscripcion: nivel coincide, hay cupo, no moroso, no inscrito previo.
     */
    public void validarInscripcion(Estudiante estudiante,
                                   Clase clase,
                                   int inscritosActuales,
                                   boolean yaInscrito,
                                   List<Pago> pagosDelEstudiante) {
        if (estudiante == null || clase == null) {
            throw new DomainException("Inscripcion requiere estudiante y clase");
        }
        if (yaInscrito) {
            throw new DomainException("El estudiante ya esta inscrito en esta clase");
        }
        if (inscritosActuales >= clase.getCupo()) {
            throw new DomainException("La clase ya esta llena");
        }
        if (!estudiante.getNivel().equals(clase.getNivel())) {
            throw new DomainException("El estudiante solo puede inscribirse en clases de su nivel");
        }
        if (esMoroso(pagosDelEstudiante)) {
            throw new DomainException("El estudiante esta moroso y no puede inscribirse");
        }
    }

    public Inscripcion crearInscripcion(Long estudianteId, Long claseId) {
        return new Inscripcion(null, estudianteId, claseId, null);
    }

    private boolean esMoroso(List<Pago> pagos) {
        if (pagos == null || pagos.isEmpty()) {
            return true; // sin pagos recientes => moroso
        }
        LocalDate hoy = LocalDate.now();
        return pagos.stream().allMatch(p -> p.esMoroso(hoy));
    }
}
