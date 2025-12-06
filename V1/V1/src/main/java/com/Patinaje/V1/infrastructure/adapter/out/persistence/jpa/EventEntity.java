package com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "eventos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, length = 2000)
    private String descripcion;

    @Column(nullable = false)
    private String categoria; // Ej: Proximo, Taller, Exhibicion

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private String ubicacion;

    private String imagenUrl;

    private boolean destacado;

    private String creadoPor;

    private LocalDateTime creadoEn;
}
