package com.Patinaje.V1.application.usecase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.Patinaje.V1.domain.model.CartItem;
import com.Patinaje.V1.domain.model.CartSummary;
import com.Patinaje.V1.domain.model.Product;

@Service
public class CartService {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.19");
    private static final BigDecimal SHIPPING_COST = new BigDecimal("15000");

    private final ProductCatalog productCatalog;
    private final Map<String, CartState> carts = new ConcurrentHashMap<>();

    public CartService(ProductCatalog productCatalog) {
        this.productCatalog = productCatalog;
    }

    public void addItem(String sessionId, Long productId, int qty) {
        if (qty <= 0) qty = 1;
        CartState state = carts.computeIfAbsent(sessionId, k -> new CartState());
        Optional<Product> productOpt = productCatalog.findById(productId);
        if (productOpt.isEmpty()) {
            return;
        }
        state.add(productOpt.get(), qty);
    }

    public void setEnvio(String sessionId, boolean envio) {
        CartState state = carts.computeIfAbsent(sessionId, k -> new CartState());
        state.setEnvio(envio);
    }

    public void clear(String sessionId) {
        carts.remove(sessionId);
    }

    public CartSummary getSummary(String sessionId) {
        CartState state = carts.getOrDefault(sessionId, new CartState());
        List<CartItem> items = state.getItems();
        BigDecimal subtotal = items.stream()
                .map(ci -> ci.getProduct().getPrecio().multiply(BigDecimal.valueOf(ci.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal impuestos = subtotal.multiply(TAX_RATE);
        BigDecimal envio = state.isEnvio() ? SHIPPING_COST : BigDecimal.ZERO;
        BigDecimal total = subtotal.add(impuestos).add(envio);
        return new CartSummary(items, subtotal, impuestos, envio, total, state.isEnvio());
    }

    private static class CartState {
        private final List<CartItem> items = new ArrayList<>();
        private boolean envio = false;

        void add(Product product, int qty) {
            for (CartItem item : items) {
                if (item.getProduct().getId().equals(product.getId())) {
                    item.addQuantity(qty);
                    return;
                }
            }
            items.add(new CartItem(product, qty));
        }

        void setEnvio(boolean envio) {
            this.envio = envio;
        }

        boolean isEnvio() {
            return envio;
        }

        List<CartItem> getItems() {
            return Collections.unmodifiableList(items);
        }
    }
}
