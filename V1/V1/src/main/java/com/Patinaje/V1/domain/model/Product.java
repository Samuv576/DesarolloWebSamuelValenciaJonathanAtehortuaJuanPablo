package com.Patinaje.V1.domain.model;

import java.math.BigDecimal;

public class Product {
    private final Long id;
    private final String nombre;
    private final BigDecimal precio;
    private final boolean requiereEnvio;

    public Product(Long id, String nombre, BigDecimal precio, boolean requiereEnvio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.requiereEnvio = requiereEnvio;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public boolean isRequiereEnvio() {
        return requiereEnvio;
    }
}
