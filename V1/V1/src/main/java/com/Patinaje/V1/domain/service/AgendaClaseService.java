package com.Patinaje.V1.domain.service;

import java.time.LocalTime;
import java.util.List;

import com.Patinaje.V1.domain.model.Clase;
import com.Patinaje.V1.shared.exception.DomainException;

public class AgendaClaseService {

    /**
     * Valida que un instructor no tenga traslapes en el mismo dia.
     */
    public void validarTraslape(Clase nueva, List<Clase> existentes) {
        if (existentes == null || existentes.isEmpty()) {
            return;
        }
        for (Clase c : existentes) {
            if (!c.getInstructorId().equals(nueva.getInstructorId())) {
                continue;
            }
            if (!c.getDia().equals(nueva.getDia())) {
                continue;
            }
            if (traslapa(c.getHoraInicio(), c.getHoraFin(), nueva.getHoraInicio(), nueva.getHoraFin())) {
                throw new DomainException("El instructor ya tiene una clase en ese horario");
            }
        }
    }

    private boolean traslapa(LocalTime i1, LocalTime f1, LocalTime i2, LocalTime f2) {
        return i1.isBefore(f2) && i2.isBefore(f1);
    }
}
