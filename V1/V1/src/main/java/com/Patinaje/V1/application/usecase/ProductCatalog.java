package com.Patinaje.V1.application.usecase;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.Patinaje.V1.domain.model.Product;

@Component
public class ProductCatalog {

    private final List<Product> products = Arrays.asList(
            new Product(1L, "Canariam Onix", new BigDecimal("219000"), true),
            new Product(2L, "Ruedas X4 80mm", new BigDecimal("45000"), true),
            new Product(3L, "Kit de protección", new BigDecimal("59900"), true),
            new Product(4L, "Curso Dominio", new BigDecimal("120000"), false),
            new Product(5L, "Membresía Comunidad", new BigDecimal("39900"), false),
            new Product(6L, "Deck Urbano", new BigDecimal("89900"), true)
    );

    public Optional<Product> findById(Long id) {
        return products.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public List<Product> findAll() {
        return products;
    }
}
