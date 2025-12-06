package com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class EventDataLoader implements CommandLineRunner {

    private final EventJpaRepository eventRepo;

    public EventDataLoader(EventJpaRepository eventRepo) {
        this.eventRepo = eventRepo;
    }

    @Override
    public void run(String... args) {
        if (eventRepo.count() > 0) return;

        eventRepo.saveAll(List.of(
                EventEntity.builder()
                        .titulo("Jornada de Evaluación de Niveles")
                        .descripcion("Sesiones para validar progreso de alumnos y asignación de nuevas clases.")
                        .categoria("Próximo")
                        .fecha(LocalDate.now().plusDays(10))
                        .ubicacion("Pista Santa Marta")
                        .destacado(true)
                        .imagenUrl("/images/evento1.png")
                        .creadoPor("admin")
                        .creadoEn(LocalDateTime.now())
                        .build(),
                EventEntity.builder()
                        .titulo("Semana de Puertas Abiertas")
                        .descripcion("Charlas de seguridad, demostraciones y registro de nuevos aspirantes.")
                        .categoria("Divulgación")
                        .fecha(LocalDate.now().plusDays(25))
                        .ubicacion("Sede Principal")
                        .destacado(false)
                        .imagenUrl("/images/evento2.png")
                        .creadoPor("admin")
                        .creadoEn(LocalDateTime.now())
                        .build()
        ));
    }
}
