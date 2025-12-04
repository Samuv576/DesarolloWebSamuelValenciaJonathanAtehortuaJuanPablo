package com.Patinaje.V1.application.usecase;

import java.util.List;

import com.Patinaje.V1.application.port.in.CrearClaseUseCase;
import com.Patinaje.V1.application.port.out.ClaseRepository;
import com.Patinaje.V1.application.port.out.InstructorRepository;
import com.Patinaje.V1.domain.model.Clase;
import com.Patinaje.V1.domain.model.Instructor;
import com.Patinaje.V1.domain.service.AgendaClaseService;
import com.Patinaje.V1.shared.exception.DomainException;

public class CrearClaseService implements CrearClaseUseCase {

    private final ClaseRepository claseRepository;
    private final InstructorRepository instructorRepository;
    private final AgendaClaseService agendaClaseService;

    public CrearClaseService(ClaseRepository claseRepository,
                             InstructorRepository instructorRepository,
                             AgendaClaseService agendaClaseService) {
        this.claseRepository = claseRepository;
        this.instructorRepository = instructorRepository;
        this.agendaClaseService = agendaClaseService;
    }

    @Override
    public Clase crear(Clase clase) {
        Instructor instructor = instructorRepository.findById(clase.getInstructorId())
                .orElseThrow(() -> new DomainException("Instructor no existe"));

        List<Clase> existentes = claseRepository.findByInstructorAndDia(instructor.getPersona().getId(), clase.getDia());
        agendaClaseService.validarTraslape(clase, existentes);

        return claseRepository.save(clase);
    }
}
