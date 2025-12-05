# Proyecto Patín Pro (Spring Boot)

Aplicación web para academia de patinaje con portales de admin, instructor, estudiante, carrito/checkout y comunidad. Construida con Spring Boot 4, Spring Security, Thymeleaf y MySQL.

## Requisitos
- Java 25
- Maven (o `mvnw.cmd` / `mvnw` incluidos)
- MySQL en `localhost:3306` con base `db_patinaje` (config en `src/main/resources/application.properties`)

## Ejecución local
```bash
# (Opcional) crear y poblar DB
# credenciales en application.properties

# build y run
./mvnw spring-boot:run
```
- La app levanta en `http://localhost:8085`.
- Usuarios seed en `UserDataLoader`: `admin/admin123`, `instructor/instructor123`, `alumno/alumno123`, etc.

## Documentación (Swagger / OpenAPI)
- Swagger UI: `http://localhost:8085/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8085/v3/api-docs`

Controladores documentados con @Tag/@Operation:
- Páginas públicas (`PageController`)
- Autenticación (`AuthController`)
- Carrito/checkout (`CartController`, `CheckoutController`)
- Comunidad (`CommunityController`)
- Admin (`AdminController`)
- Portal estudiante (`StudentPortalController`)
- Portal instructor (`InstructorPortalController`)

## Testing
No se incluyen tests automatizados; ejecutar `./mvnw test` si agregas pruebas.

## Notas
- Seed inicial crea usuarios, instructores, alumnos, clases, pagos, inscripciones y grupos de comunidad.
- Seguridad: roles ADMINISTRADOR, INSTRUCTOR, ALUMNO; CSRF desactivado para simplicidad en demo.

## Deploy rápido (Railway/Render/Fly)
- El puerto ya se toma de `PORT` (`server.port=${PORT:8085}`).
- Variables esperadas en producción:
  - `SPRING_DATASOURCE_URL`
  - `SPRING_DATASOURCE_USERNAME`
  - `SPRING_DATASOURCE_PASSWORD`
  - (opcional) `SPRING_JPA_HIBERNATE_DDL_AUTO`
- Incluye `Dockerfile` multistage:
  ```bash
  docker build -t patin-pro .
  docker run -p 8085:8085 \
    -e SPRING_DATASOURCE_URL=jdbc:mysql://host:3306/db \
    -e SPRING_DATASOURCE_USERNAME=user \
    -e SPRING_DATASOURCE_PASSWORD=pass \
    patin-pro
  ```
- En Railway/Render: conecta el repo, usa Dockerfile o el buildpack Java; añade las variables de DB que entregue el servicio MySQL/Postgres y publica.
