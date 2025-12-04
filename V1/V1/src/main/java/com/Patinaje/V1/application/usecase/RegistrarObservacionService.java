package com.Patinaje.V1.application.usecase;

import com.Patinaje.V1.application.port.in.RegistrarObservacionUseCase;
import com.Patinaje.V1.application.port.out.ObservacionRepository;
import com.Patinaje.V1.domain.model.ObservacionProgreso;
import com.Patinaje.V1.shared.exception.DomainException;

public class RegistrarObservacionService implements RegistrarObservacionUseCase {

    private final ObservacionRepository observacionRepository;

    public RegistrarObservacionService(ObservacionRepository observacionRepository) {
        this.observacionRepository = observacionRepository;
    }

    @Override
    public ObservacionProgreso registrar(Long estudianteId,
                                         Long instructorId,
                                         int control,
                                         int velocidad,
                                         int equilibrio,
                                         int frenado,
                                         String nota) {
        if (estudianteId == null || instructorId == null) {
            throw new DomainException("Estudiante e instructor son obligatorios");
        }
        ObservacionProgreso obs = new ObservacionProgreso(
                null,
                estudianteId,
                instructorId,
                null,
                control,
                velocidad,
                equilibrio,
                frenado,
                nota
        );
        return observacionRepository.save(obs);
    }
}
