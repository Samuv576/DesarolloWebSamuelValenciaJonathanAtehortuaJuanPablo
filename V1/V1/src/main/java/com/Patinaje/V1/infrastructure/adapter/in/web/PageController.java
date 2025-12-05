package com.Patinaje.V1.infrastructure.adapter.in.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.InstructorJpaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@Tag(name = "Paginas publicas", description = "Rutas de landing y contenido informativo")
public class PageController {

    private final InstructorJpaRepository instructorRepo;

    public PageController(InstructorJpaRepository instructorRepo) {
        this.instructorRepo = instructorRepo;
    }

    // Home
    @GetMapping({"/", "/home", "/index", "/index/home"})
    @Operation(summary = "Pagina principal", description = "Landing page con hero, servicios y acceso a tienda.")
    public String home() {
        return "index";
    }

    // Paginas publicas
    @GetMapping("/tienda")
    @Operation(summary = "Tienda", description = "Catalogo comercial.")
    public String tienda() {
        return "tienda";
    }

    @GetMapping("/producto")
    @Operation(summary = "Detalle de producto", description = "Vista de un producto de ejemplo.")
    public String producto() {
        return "producto";
    }

    @GetMapping("/contactar")
    @Operation(summary = "Contacto", description = "Formulario de contacto general.")
    public String contactar() {
        return "contactar";
    }

    @GetMapping("/clases")
    @Operation(summary = "Clases publicas", description = "Descripcion de clases ofertadas.")
    public String clases() {
        return "clases";
    }

    @GetMapping("/instructores")
    @Operation(summary = "Listado de instructores", description = "Muestra instructores registrados para marketing.")
    public String instructores(Model model) {
        model.addAttribute("instructores", instructorRepo.findAll());
        return "instructores";
    }

    @GetMapping("/institucional")
    @Operation(summary = "Institucional", description = "Informacion corporativa.")
    public String institucional() {
        return "institucional";
    }

    @GetMapping("/sobre-nosotros")
    @Operation(summary = "Sobre nosotros", description = "Historia y proposito.")
    public String sobreNosotros() {
        return "sobre_nosotros";
    }

    @GetMapping("/politica-privacidad")
    @Operation(summary = "Politica de privacidad")
    public String politicaPrivacidad() {
        return "politica_privacidad";
    }

    @GetMapping("/preguntas-frecuentes")
    @Operation(summary = "Preguntas frecuentes")
    public String preguntasFrecuentes() {
        return "preguntas_frecuentes";
    }

    @GetMapping("/terminos")
    @Operation(summary = "Terminos y condiciones")
    public String terminos() {
        return "terminos";
    }

    @GetMapping("/aviso_legal")
    @Operation(summary = "Aviso legal")
    public String avisoLegal() {
        return "aviso_legal";
    }

    @GetMapping("/login")
    @Operation(summary = "Login", description = "Pagina de autenticacion.")
    public String login() {
        return "login";
    }
}
