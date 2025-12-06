package com.Patinaje.V1.domain.model;

import java.math.BigDecimal;

public class Product {
    private final Long id;
    private final String nombre;
    private final String descripcion;
    private final String categoria;
    private final BigDecimal precio;
    private final boolean requiereEnvio;
    private final String imagenUrl;

    public Product(Long id, String nombre, String descripcion, String categoria,
                   BigDecimal precio, boolean requiereEnvio, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.precio = precio;
        this.requiereEnvio = requiereEnvio;
        this.imagenUrl = imagenUrl;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public boolean isRequiereEnvio() {
        return requiereEnvio;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }
}
