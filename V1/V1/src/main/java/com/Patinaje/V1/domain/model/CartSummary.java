package com.Patinaje.V1.domain.model;

import java.math.BigDecimal;
import java.util.List;

public class CartSummary {
    private final List<CartItem> items;
    private final BigDecimal subtotal;
    private final BigDecimal impuestos;
    private final BigDecimal envio;
    private final BigDecimal total;
    private final boolean envioSeleccionado;

    public CartSummary(List<CartItem> items, BigDecimal subtotal, BigDecimal impuestos, BigDecimal envio, BigDecimal total, boolean envioSeleccionado) {
        this.items = items;
        this.subtotal = subtotal;
        this.impuestos = impuestos;
        this.envio = envio;
        this.total = total;
        this.envioSeleccionado = envioSeleccionado;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getImpuestos() {
        return impuestos;
    }

    public BigDecimal getEnvio() {
        return envio;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public boolean isEnvioSeleccionado() {
        return envioSeleccionado;
    }
}
