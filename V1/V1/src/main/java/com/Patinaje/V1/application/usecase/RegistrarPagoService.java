package com.Patinaje.V1.application.usecase;

import java.math.BigDecimal;

import com.Patinaje.V1.application.port.in.RegistrarPagoUseCase;
import com.Patinaje.V1.application.port.out.PagoRepository;
import com.Patinaje.V1.domain.model.EstadoPago;
import com.Patinaje.V1.domain.model.MedioPago;
import com.Patinaje.V1.domain.model.Pago;
import com.Patinaje.V1.shared.exception.DomainException;

public class RegistrarPagoService implements RegistrarPagoUseCase {

    private final PagoRepository pagoRepository;

    public RegistrarPagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    @Override
    public Pago registrar(Long estudianteId, BigDecimal monto, MedioPago medioPago) {
        if (estudianteId == null) {
            throw new DomainException("El estudiante es obligatorio");
        }
        Pago pago = new Pago(null, estudianteId, monto, null, medioPago, EstadoPago.PENDIENTE);
        return pagoRepository.save(pago);
    }
}
