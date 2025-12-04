package com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa;

import com.Patinaje.V1.domain.model.Role;
import com.Patinaje.V1.domain.model.Genero;
import com.Patinaje.V1.domain.model.Nivel;
import com.Patinaje.V1.domain.model.MedioPago;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserDataLoader {

    @Bean
    CommandLineRunner seedUsers(UserJpaRepository userRepo,
                               StudentJpaRepository studentRepo,
                               InstructorJpaRepository instructorRepo,
                               ClassJpaRepository classRepo,
                               PasswordEncoder encoder) {
        return args -> {
            if (userRepo.findByUsername("admin").isEmpty()) {
                userRepo.save(UserEntity.builder()
                        .username("admin")
                        .password(encoder.encode("admin123"))
                        .role(Role.ADMINISTRADOR)
                        .enabled(true)
                        .build());
            }
            if (userRepo.findByUsername("admin2").isEmpty()) {
                userRepo.save(UserEntity.builder()
                        .username("admin2")
                        .password(encoder.encode("admin123"))
                        .role(Role.ADMINISTRADOR)
                        .enabled(true)
                        .build());
            }
            if (userRepo.findByUsername("instructor").isEmpty()) {
                var userInstr = userRepo.save(UserEntity.builder()
                        .username("instructor")
                        .password(encoder.encode("instructor123"))
                        .role(Role.INSTRUCTOR)
                        .enabled(true)
                        .build());
                var instructor = instructorRepo.save(InstructorEntity.builder()
                        .nombre("Instructor Demo")
                        .identificacion("I123")
                        .fechaNacimiento(java.time.LocalDate.of(1990,1,1))
                        .genero(Genero.MASCULINO)
                        .telefono("3100000000")
                        .correo("instructor@demo.com")
                        .direccion("Santa Marta")
                        .especialidad("Velocidad")
                        .build());
                if (classRepo.count() == 0) {
                    classRepo.save(ClassEntity.builder()
                            .nombre("Clase Básica")
                            .nivel(Nivel.BASICO)
                            .dia(java.time.DayOfWeek.MONDAY)
                            .horaInicio(java.time.LocalTime.of(9,0))
                            .horaFin(java.time.LocalTime.of(10,0))
                            .cupo(20)
                            .instructor(instructor)
                            .build());
                }
            }
            if (userRepo.findByUsername("instructor2").isEmpty()) {
                userRepo.save(UserEntity.builder()
                        .username("instructor2")
                        .password(encoder.encode("instructor123"))
                        .role(Role.INSTRUCTOR)
                        .enabled(true)
                        .build());
            }
            if (userRepo.findByUsername("alumno").isEmpty()) {
                var userAlumno = userRepo.save(UserEntity.builder()
                        .username("alumno")
                        .password(encoder.encode("alumno123"))
                        .role(Role.ALUMNO)
                        .enabled(true)
                        .build());
                if (studentRepo.findByIdentificacion("A123").isEmpty()) {
                    studentRepo.save(StudentEntity.builder()
                            .nombre("Alumno Demo")
                            .identificacion("A123")
                            .fechaNacimiento(java.time.LocalDate.of(2005,1,1))
                            .genero(Genero.MASCULINO)
                            .telefono("3000000000")
                            .correo("alumno@demo.com")
                            .direccion("Santa Marta")
                            .nivel(Nivel.BASICO)
                            .medioPago(MedioPago.TRANSFERENCIA)
                            .contactoEmergencia("Contacto Demo")
                            .build());
                }
            }
            if (userRepo.findByUsername("alumno2").isEmpty()) {
                userRepo.save(UserEntity.builder()
                        .username("alumno2")
                        .password(encoder.encode("alumno123"))
                        .role(Role.ALUMNO)
                        .enabled(true)
                        .build());
            }
        };
    }
}
