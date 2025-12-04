package com.Patinaje.V1.application.usecase;

import java.util.List;

import com.Patinaje.V1.application.port.in.InscribirEnClaseUseCase;
import com.Patinaje.V1.application.port.out.ClaseRepository;
import com.Patinaje.V1.application.port.out.EstudianteRepository;
import com.Patinaje.V1.application.port.out.InscripcionRepository;
import com.Patinaje.V1.application.port.out.PagoRepository;
import com.Patinaje.V1.domain.model.Clase;
import com.Patinaje.V1.domain.model.Estudiante;
import com.Patinaje.V1.domain.model.Inscripcion;
import com.Patinaje.V1.domain.model.Pago;
import com.Patinaje.V1.domain.service.InscripcionService;
import com.Patinaje.V1.shared.exception.DomainException;

public class InscribirEnClaseService implements InscribirEnClaseUseCase {

    private final EstudianteRepository estudianteRepository;
    private final ClaseRepository claseRepository;
    private final InscripcionRepository inscripcionRepository;
    private final PagoRepository pagoRepository;
    private final InscripcionService inscripcionService;

    public InscribirEnClaseService(EstudianteRepository estudianteRepository,
                                   ClaseRepository claseRepository,
                                   InscripcionRepository inscripcionRepository,
                                   PagoRepository pagoRepository,
                                   InscripcionService inscripcionService) {
        this.estudianteRepository = estudianteRepository;
        this.claseRepository = claseRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.pagoRepository = pagoRepository;
        this.inscripcionService = inscripcionService;
    }

    @Override
    public Inscripcion inscribir(Long estudianteId, Long claseId) {
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new DomainException("Estudiante no existe"));
        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new DomainException("Clase no existe"));

        boolean yaInscrito = inscripcionRepository.existsByClaseIdAndEstudianteId(claseId, estudianteId);
        int inscritos = claseRepository.countInscritos(claseId);
        List<Pago> pagos = pagoRepository.findByEstudianteId(estudianteId);

        inscripcionService.validarInscripcion(estudiante, clase, inscritos, yaInscrito, pagos);

        Inscripcion inscripcion = inscripcionService.crearInscripcion(estudianteId, claseId);
        return inscripcionRepository.save(inscripcion);
    }
}
