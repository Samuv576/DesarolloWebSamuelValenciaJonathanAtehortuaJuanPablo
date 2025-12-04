package com.Patinaje.V1.application.port.in;

import com.Patinaje.V1.domain.model.Inscripcion;

public interface InscribirEnClaseUseCase {
    Inscripcion inscribir(Long estudianteId, Long claseId);
}
