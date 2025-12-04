package com.Patinaje.V1.application.usecase;

import com.Patinaje.V1.application.port.in.RegistrarEstudianteUseCase;
import com.Patinaje.V1.application.port.out.EstudianteRepository;
import java.time.LocalDate;

import com.Patinaje.V1.domain.model.Estudiante;
import com.Patinaje.V1.domain.model.Genero;
import com.Patinaje.V1.domain.model.MedioPago;
import com.Patinaje.V1.domain.model.Nivel;
import com.Patinaje.V1.domain.model.Persona;
import com.Patinaje.V1.shared.exception.DomainException;

public class RegistrarEstudianteService implements RegistrarEstudianteUseCase {

    private final EstudianteRepository estudianteRepository;

    public RegistrarEstudianteService(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    @Override
    public Estudiante registrar(String nombre,
                                String identificacion,
                                LocalDate fechaNacimiento,
                                Genero genero,
                                String telefono,
                                String correo,
                                String direccion,
                                Nivel nivel,
                                String contactoEmergencia,
                                MedioPago medioPago) {
        estudianteRepository.findByIdentificacion(identificacion)
                .ifPresent(e -> { throw new DomainException("Identificacion ya registrada"); });

        Persona persona = new Persona(null, nombre, identificacion, fechaNacimiento, genero, telefono, correo, direccion);
        Estudiante estudiante = new Estudiante(persona, nivel, medioPago, contactoEmergencia);
        return estudianteRepository.save(estudiante);
    }
}
