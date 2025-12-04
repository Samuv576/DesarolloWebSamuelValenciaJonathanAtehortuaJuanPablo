package com.Patinaje.V1.application.port.in;

import java.time.LocalDate;

import com.Patinaje.V1.domain.model.Estudiante;
import com.Patinaje.V1.domain.model.Genero;
import com.Patinaje.V1.domain.model.MedioPago;
import com.Patinaje.V1.domain.model.Nivel;

public interface RegistrarEstudianteUseCase {
    Estudiante registrar(String nombre,
                         String identificacion,
                         LocalDate fechaNacimiento,
                         Genero genero,
                         String telefono,
                         String correo,
                         String direccion,
                         Nivel nivel,
                         String contactoEmergencia,
                         MedioPago medioPago);
}
