package com.Patinaje.V1.application.port.in;

import java.time.LocalDate;

import com.Patinaje.V1.domain.model.Genero;
import com.Patinaje.V1.domain.model.Instructor;

public interface RegistrarInstructorUseCase {
    Instructor registrar(String nombre,
                         String identificacion,
                         LocalDate fechaNacimiento,
                         Genero genero,
                         String telefono,
                         String correo,
                         String direccion,
                         String especialidad);
}
