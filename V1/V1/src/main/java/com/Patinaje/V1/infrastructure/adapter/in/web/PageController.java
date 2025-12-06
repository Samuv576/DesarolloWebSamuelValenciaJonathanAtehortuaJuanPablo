package com.Patinaje.V1.infrastructure.adapter.in.web;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.Patinaje.V1.application.usecase.ProductCatalog;
import com.Patinaje.V1.domain.model.Product;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.InstructorJpaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@Tag(name = "Paginas publicas", description = "Rutas de landing y contenido informativo")
public class PageController {

    private final InstructorJpaRepository instructorRepo;
    private final ProductCatalog productCatalog;

    public PageController(InstructorJpaRepository instructorRepo, ProductCatalog productCatalog) {
        this.instructorRepo = instructorRepo;
        this.productCatalog = productCatalog;
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
    public String tienda(@RequestParam(name = "categoria", required = false) String categoria,
                         @RequestParam(name = "sort", defaultValue = "nuevo") String sort,
                         @RequestParam(name = "page", defaultValue = "1") int page,
                         @RequestParam(name = "size", defaultValue = "6") int size,
                         Model model) {
        var allProducts = productCatalog.findAll();

        var categorias = allProducts.stream()
                .map(p -> p.getCategoria() == null ? "" : p.getCategoria())
                .filter(c -> !c.isBlank())
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());

        var filtered = allProducts.stream()
                .filter(p -> categoria == null || categoria.isBlank() || p.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());

        Comparator<Product> comparator;
        switch (sort) {
            case "precioAsc" -> comparator = Comparator.comparing(Product::getPrecio);
            case "precioDesc" -> comparator = Comparator.comparing(Product::getPrecio).reversed();
            default -> {
                comparator = Comparator.comparing(Product::getId).reversed();
                sort = "nuevo";
            }
        }
        filtered.sort(comparator);

        int total = filtered.size();
        if (size <= 0) size = 6;
        int totalPages = (int) Math.ceil(total / (double) size);
        int currentPage = Math.max(1, page);
        if (totalPages > 0 && currentPage > totalPages) {
            currentPage = totalPages;
        }

        int fromIndex = Math.max(0, (currentPage - 1) * size);
        int toIndex = Math.min(fromIndex + size, total);
        List<Product> paged = total == 0 ? List.of() : filtered.subList(fromIndex, toIndex);
        int startItem = total == 0 ? 0 : fromIndex + 1;
        int endItem = total == 0 ? 0 : toIndex;

        model.addAttribute("products", paged);
        model.addAttribute("categories", categorias);
        model.addAttribute("selectedCategoria", categoria == null ? "" : categoria);
        model.addAttribute("sort", sort);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalProducts", total);
        model.addAttribute("pageSize", size);
        model.addAttribute("startItem", startItem);
        model.addAttribute("endItem", endItem);
        return "tienda";
    }

    @GetMapping("/producto/{id}")
    @Operation(summary = "Detalle de producto", description = "Vista del producto seleccionado desde la tienda.")
    public String producto(@PathVariable Long id, Model model) {
        var productoOpt = productCatalog.findById(id);
        if (productoOpt.isEmpty()) {
            return "redirect:/tienda?productoNoEncontrado";
        }
        model.addAttribute("producto", productoOpt.get());
        model.addAttribute("relacionados", productCatalog.findAll().stream()
                .filter(p -> !p.getId().equals(id))
                .limit(4)
                .collect(Collectors.toList()));
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
