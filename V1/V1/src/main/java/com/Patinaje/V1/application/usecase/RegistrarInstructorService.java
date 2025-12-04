package com.Patinaje.V1.application.usecase;

import java.time.LocalDate;

import com.Patinaje.V1.application.port.in.RegistrarInstructorUseCase;
import com.Patinaje.V1.application.port.out.InstructorRepository;
import com.Patinaje.V1.domain.model.Genero;
import com.Patinaje.V1.domain.model.Instructor;
import com.Patinaje.V1.domain.model.Persona;
import com.Patinaje.V1.shared.exception.DomainException;

public class RegistrarInstructorService implements RegistrarInstructorUseCase {

    private final InstructorRepository instructorRepository;

    public RegistrarInstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @Override
    public Instructor registrar(String nombre,
                                String identificacion,
                                LocalDate fechaNacimiento,
                                Genero genero,
                                String telefono,
                                String correo,
                                String direccion,
                                String especialidad) {
        instructorRepository.findByIdentificacion(identificacion)
                .ifPresent(e -> { throw new DomainException("Identificacion ya registrada"); });

        Persona persona = new Persona(null, nombre, identificacion, fechaNacimiento, genero, telefono, correo, direccion);
        Instructor instructor = new Instructor(persona, especialidad);
        return instructorRepository.save(instructor);
    }
}
