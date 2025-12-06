package com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "observaciones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObservacionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private StudentEntity estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    private InstructorEntity instructor;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private int control;

    @Column(nullable = false)
    private int velocidad;

    @Column(nullable = false)
    private int equilibrio;

    @Column(nullable = false)
    private int frenado;

    @Column(nullable = false, length = 2000)
    private String nota;
}
