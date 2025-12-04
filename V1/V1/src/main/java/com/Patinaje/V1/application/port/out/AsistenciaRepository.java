package com.Patinaje.V1.application.port.out;

import com.Patinaje.V1.domain.model.Asistencia;

public interface AsistenciaRepository {
    Asistencia save(Asistencia asistencia);
}
