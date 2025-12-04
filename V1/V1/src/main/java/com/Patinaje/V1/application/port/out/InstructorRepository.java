package com.Patinaje.V1.application.port.out;

import java.util.Optional;

import com.Patinaje.V1.domain.model.Instructor;

public interface InstructorRepository {
    Optional<Instructor> findById(Long id);
    Optional<Instructor> findByIdentificacion(String identificacion);
    Instructor save(Instructor instructor);
}
