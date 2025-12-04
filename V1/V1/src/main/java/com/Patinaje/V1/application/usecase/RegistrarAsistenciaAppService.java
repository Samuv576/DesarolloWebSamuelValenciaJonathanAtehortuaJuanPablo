package com.Patinaje.V1.application.usecase;

import java.time.LocalDate;
import java.util.Set;

import com.Patinaje.V1.application.port.in.RegistrarAsistenciaUseCase;
import com.Patinaje.V1.application.port.out.AsistenciaRepository;
import com.Patinaje.V1.application.port.out.InscripcionRepository;
import com.Patinaje.V1.domain.model.Asistencia;
import com.Patinaje.V1.domain.service.AsistenciaService;

public class RegistrarAsistenciaAppService implements RegistrarAsistenciaUseCase {

    private final InscripcionRepository inscripcionRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final AsistenciaService asistenciaService;

    public RegistrarAsistenciaAppService(InscripcionRepository inscripcionRepository,
                                         AsistenciaRepository asistenciaRepository,
                                         AsistenciaService asistenciaService) {
        this.inscripcionRepository = inscripcionRepository;
        this.asistenciaRepository = asistenciaRepository;
        this.asistenciaService = asistenciaService;
    }

    @Override
    public Asistencia registrar(Long claseId, Long estudianteId, boolean presente) {
        Set<Long> inscritos = inscripcionRepository.findEstudiantesIdsByClase(claseId);
        Asistencia asistencia = asistenciaService.registrarAsistencia(claseId, estudianteId, LocalDate.now(), presente, inscritos);
        return asistenciaRepository.save(asistencia);
    }
}
