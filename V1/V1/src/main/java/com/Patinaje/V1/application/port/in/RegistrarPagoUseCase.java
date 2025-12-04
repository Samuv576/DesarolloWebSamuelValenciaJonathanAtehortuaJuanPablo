package com.Patinaje.V1.application.port.in;

import java.math.BigDecimal;

import com.Patinaje.V1.domain.model.MedioPago;
import com.Patinaje.V1.domain.model.Pago;

public interface RegistrarPagoUseCase {
    Pago registrar(Long estudianteId, BigDecimal monto, MedioPago medioPago);
}
