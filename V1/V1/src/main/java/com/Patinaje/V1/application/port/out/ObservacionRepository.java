package com.Patinaje.V1.application.port.out;

import com.Patinaje.V1.domain.model.ObservacionProgreso;

public interface ObservacionRepository {
    ObservacionProgreso save(ObservacionProgreso observacion);
}
