package com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ProductDataLoader implements CommandLineRunner {

    private final ProductJpaRepository productRepo;

    public ProductDataLoader(ProductJpaRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public void run(String... args) {
        if (productRepo.count() > 0) {
            return;
        }

        productRepo.saveAll(List.of(
                ProductEntity.builder()
                        .nombre("Canariam Onix")
                        .descripcion("Patines freeskate de alto rendimiento con carcasa rigida y rodamiento rapido.")
                        .categoria("Patines")
                        .precio(new BigDecimal("219000"))
                        .requiereEnvio(true)
                        .imagenUrl("/images/canariam.png")
                        .build(),
                ProductEntity.builder()
                        .nombre("Ruedas X4 80mm")
                        .descripcion("Set de 4 ruedas 80mm 85A para patinaje urbano, agarre y durabilidad.")
                        .categoria("Ruedas")
                        .precio(new BigDecimal("45000"))
                        .requiereEnvio(true)
                        .imagenUrl("/images/ruedas.png")
                        .build(),
                ProductEntity.builder()
                        .nombre("Kit de proteccion")
                        .descripcion("Rodilleras, coderas y munqueras con ajuste ergonomico.")
                        .categoria("Proteccion")
                        .precio(new BigDecimal("59900"))
                        .requiereEnvio(true)
                        .imagenUrl("/images/proteccion.png")
                        .build(),
                ProductEntity.builder()
                        .nombre("Curso Dominio")
                        .descripcion("Programa intensivo de dominio y control en rampas y slalom.")
                        .categoria("Clases")
                        .precio(new BigDecimal("120000"))
                        .requiereEnvio(false)
                        .imagenUrl("/images/dominio.png")
                        .build(),
                ProductEntity.builder()
                        .nombre("Membresia Comunidad")
                        .descripcion("Acceso a grupos, retos y eventos exclusivos de la comunidad.")
                        .categoria("Comunidad")
                        .precio(new BigDecimal("39900"))
                        .requiereEnvio(false)
                        .imagenUrl("/images/comunidad.png")
                        .build(),
                ProductEntity.builder()
                        .nombre("Deck Urbano")
                        .descripcion("Deck resistente para freeride con buen pop y superficie antideslizante.")
                        .categoria("Decks")
                        .precio(new BigDecimal("89900"))
                        .requiereEnvio(true)
                        .imagenUrl("/images/calles.png")
                        .build()
        ));
    }
}
