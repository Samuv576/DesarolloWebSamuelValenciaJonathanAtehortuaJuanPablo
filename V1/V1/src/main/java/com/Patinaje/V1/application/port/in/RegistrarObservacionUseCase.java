package com.Patinaje.V1.application.port.in;

import com.Patinaje.V1.domain.model.ObservacionProgreso;

public interface RegistrarObservacionUseCase {
    ObservacionProgreso registrar(Long estudianteId,
                                  Long instructorId,
                                  int control,
                                  int velocidad,
                                  int equilibrio,
                                  int frenado,
                                  String nota);
}
