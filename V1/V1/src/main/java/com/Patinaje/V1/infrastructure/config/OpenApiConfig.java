package com.Patinaje.V1.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI patinajeOpenApi() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Patín Pro - API y rutas web")
                        .description("Documentación generada con Springdoc para las rutas públicas, comunidad y portales administrativos.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo Patín Pro")
                                .email("soporte@patinpro.local"))
                        .license(new License().name("MIT")))
                .externalDocs(new ExternalDocumentation()
                        .description("Repositorio del proyecto")
                        .url("https://example.com/patin-pro"));
    }
}
