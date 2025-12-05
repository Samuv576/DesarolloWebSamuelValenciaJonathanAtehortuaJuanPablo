package com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa;

import com.Patinaje.V1.domain.model.Nivel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.time.LocalDate;

@Entity
@Table(name = "clases")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Nivel nivel;

    @Column
    private String diasSemana; // lista separada por coma de días (LUNES,MARTES,...)

    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column(nullable = false)
    private LocalTime horaFin;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @Column(nullable = false)
    private int cupo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    private InstructorEntity instructor;
}
