package com.Patinaje.V1.application.port.in;

import com.Patinaje.V1.domain.model.Asistencia;

public interface RegistrarAsistenciaUseCase {
    Asistencia registrar(Long claseId, Long estudianteId, boolean presente);
}
