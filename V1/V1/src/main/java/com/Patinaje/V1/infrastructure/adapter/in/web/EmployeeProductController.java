package com.Patinaje.V1.infrastructure.adapter.in.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.ProductEntity;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.ProductJpaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping("/empleado")
@Tag(name = "Empleado productos", description = "Panel para crear/editar productos")
public class EmployeeProductController {

    private final ProductJpaRepository productRepo;
    private final Path imagesDir = Path.of("src/main/resources/static/images");

    public EmployeeProductController(ProductJpaRepository productRepo) {
        this.productRepo = productRepo;
    }

    @GetMapping("/productos")
    @Operation(summary = "Listado de productos para empleado")
    public String listado(Model model) {
        model.addAttribute("productos", productRepo.findAll());
        return "empleado/productos";
    }

    @GetMapping("/productos/new")
    @Operation(summary = "Formulario nuevo producto")
    public String nuevo(Model model) {
        model.addAttribute("producto", new ProductEntity());
        model.addAttribute("isEdit", false);
        return "empleado/producto_form";
    }

    @GetMapping("/productos/{id}/edit")
    @Operation(summary = "Editar producto")
    public String editar(@PathVariable Long id, Model model) {
        var producto = productRepo.findById(id).orElse(null);
        if (producto == null) return "redirect:/empleado/productos";
        model.addAttribute("producto", producto);
        model.addAttribute("isEdit", true);
        return "empleado/producto_form";
    }

    @PostMapping("/productos/save")
    @Operation(summary = "Guardar producto con opcion de imagen")
    public String guardar(ProductEntity form,
                          @RequestParam(name = "imagenArchivo", required = false) MultipartFile imagenArchivo,
                          @RequestParam(name = "imagenActual", required = false) String imagenActual) {
        ProductEntity entity = form.getId() != null
                ? productRepo.findById(form.getId()).orElse(new ProductEntity())
                : new ProductEntity();

        entity.setNombre(form.getNombre());
        entity.setDescripcion(form.getDescripcion());
        entity.setCategoria(form.getCategoria());
        entity.setPrecio(form.getPrecio());
        entity.setRequiereEnvio(form.isRequiereEnvio());

        String imagenUrl = imagenActual;
        if (imagenArchivo != null && !imagenArchivo.isEmpty()) {
            imagenUrl = guardarImagen(imagenArchivo);
        }
        entity.setImagenUrl(imagenUrl);

        productRepo.save(entity);
        return "redirect:/empleado/productos";
    }

    @PostMapping("/productos/{id}/delete")
    @Operation(summary = "Eliminar producto")
    public String eliminar(@PathVariable Long id) {
        productRepo.deleteById(id);
        return "redirect:/empleado/productos";
    }

    private String guardarImagen(MultipartFile file) {
        try {
            if (!Files.exists(imagesDir)) {
                Files.createDirectories(imagesDir);
            }
            String cleaned = file.getOriginalFilename() == null ? "imagen" : file.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]", "_");
            String filename = LocalDateTime.now().toString().replaceAll("[:.]", "") + "_" + cleaned;
            Path target = imagesDir.resolve(filename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return "/images/" + filename;
        } catch (IOException e) {
            // En caso de fallo, no romper flujo
            return imagenActualFallback(file);
        }
    }

    private String imagenActualFallback(MultipartFile file) {
        String cleaned = file.getOriginalFilename() == null ? "imagen" : file.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]", "_");
        return "/images/" + cleaned;
    }
}
