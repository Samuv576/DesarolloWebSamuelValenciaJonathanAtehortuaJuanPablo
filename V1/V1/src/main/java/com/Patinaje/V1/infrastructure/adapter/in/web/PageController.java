package com.Patinaje.V1.infrastructure.adapter.in.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // Home
    @GetMapping({"/", "/home", "/index", "/index/home"})
    public String home() {
        return "index";
    }

    // Páginas públicas
    @GetMapping("/tienda")
    public String tienda() {
        return "tienda";
    }

    @GetMapping("/producto")
    public String producto() {
        return "producto";
    }

    @GetMapping("/contactar")
    public String contactar() {
        return "contactar";
    }

    @GetMapping("/clases")
    public String clases() {
        return "clases";
    }

    @GetMapping("/instructores")
    public String instructores() {
        return "instructores";
    }

    @GetMapping("/institucional")
    public String institucional() {
        return "institucional";
    }

    @GetMapping("/sobre-nosotros")
    public String sobreNosotros() {
        return "sobre_nosotros";
    }

    @GetMapping("/politica-privacidad")
    public String politicaPrivacidad() {
        return "politica_privacidad";
    }

    @GetMapping("/preguntas-frecuentes")
    public String preguntasFrecuentes() {
        return "preguntas_frecuentes";
    }

    @GetMapping("/terminos")
    public String terminos() {
        return "terminos";
    }

    @GetMapping("/aviso_legal")
    public String avisoLegal() {
        return "aviso_legal";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/comunidad")
    public String comunidad() {
        return "index"; // placeholder hasta tener página de comunidad
    }

}
