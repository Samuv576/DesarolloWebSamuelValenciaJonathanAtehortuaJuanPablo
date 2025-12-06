package com.Patinaje.V1.application.usecase;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.Patinaje.V1.domain.model.Product;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.ProductEntity;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.ProductJpaRepository;

@Component
public class ProductCatalog {

    private final ProductJpaRepository productRepo;

    public ProductCatalog(ProductJpaRepository productRepo) {
        this.productRepo = productRepo;
    }

    public Optional<Product> findById(Long id) {
        return productRepo.findById(id).map(this::toDomain);
    }

    public List<Product> findAll() {
        return productRepo.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private Product toDomain(ProductEntity entity) {
        return new Product(
                entity.getId(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getCategoria(),
                entity.getPrecio(),
                entity.isRequiereEnvio(),
                entity.getImagenUrl()
        );
    }
}
