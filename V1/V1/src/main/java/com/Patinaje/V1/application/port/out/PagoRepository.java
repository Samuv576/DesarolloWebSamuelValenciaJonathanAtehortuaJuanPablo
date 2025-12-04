package com.Patinaje.V1.application.port.out;

import java.util.List;

import com.Patinaje.V1.domain.model.Pago;

public interface PagoRepository {
    List<Pago> findByEstudianteId(Long estudianteId);
    Pago save(Pago pago);
}
