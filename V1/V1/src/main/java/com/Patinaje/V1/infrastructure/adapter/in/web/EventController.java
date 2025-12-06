package com.Patinaje.V1.infrastructure.adapter.in.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.EventEntity;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.EventJpaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@Tag(name = "Eventos", description = "Listado publico y gestion interna de eventos")
public class EventController {

    private final EventJpaRepository eventRepo;
    private final Path imagesDir = Path.of("src/main/resources/static/images");

    public EventController(EventJpaRepository eventRepo) {
        this.eventRepo = eventRepo;
    }

    // Publico
    @GetMapping("/eventos")
    @Operation(summary = "Listado publico de eventos")
    public String eventos(Model model) {
        model.addAttribute("eventos", eventRepo.findAll());
        return "eventos";
    }

    // Admin: listado y CRUD completo
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/admin/eventos")
    @Operation(summary = "Listado de eventos para admin")
    public String adminEventos(Model model) {
        model.addAttribute("eventos", eventRepo.findAll());
        return "admin/eventos";
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/admin/eventos/new")
    @Operation(summary = "Nuevo evento (admin)")
    public String adminNuevoEvento(Model model) {
        model.addAttribute("evento", new EventEntity());
        model.addAttribute("isEdit", false);
        return "admin/evento_form";
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/admin/eventos/{id}/edit")
    @Operation(summary = "Editar evento (admin)")
    public String adminEditarEvento(@PathVariable Long id, Model model) {
        var evento = eventRepo.findById(id).orElse(null);
        if (evento == null) return "redirect:/admin/eventos";
        model.addAttribute("evento", evento);
        model.addAttribute("isEdit", true);
        return "admin/evento_form";
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/admin/eventos/save")
    @Operation(summary = "Guardar evento (admin)")
    public String adminGuardarEvento(EventEntity form,
                                     @RequestParam(name = "imagenArchivo", required = false) MultipartFile imagenArchivo,
                                     @RequestParam(name = "imagenActual", required = false) String imagenActual) {
        guardarOActualizar(form, imagenArchivo, imagenActual, "admin");
        return "redirect:/admin/eventos";
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/admin/eventos/{id}/delete")
    @Operation(summary = "Eliminar evento (admin)")
    public String adminEliminarEvento(@PathVariable Long id) {
        eventRepo.deleteById(id);
        return "redirect:/admin/eventos";
    }

    // Empleado: solo crear
    @PreAuthorize("hasAnyRole('EMPLEADO','ADMINISTRADOR')")
    @GetMapping("/empleado/eventos/new")
    @Operation(summary = "Nuevo evento (empleado)")
    public String empleadoNuevoEvento(Model model) {
        model.addAttribute("evento", new EventEntity());
        model.addAttribute("isEdit", false);
        return "empleado/evento_form";
    }

    @PreAuthorize("hasAnyRole('EMPLEADO','ADMINISTRADOR')")
    @PostMapping("/empleado/eventos/save")
    @Operation(summary = "Guardar evento (empleado crea)")
    public String empleadoGuardarEvento(EventEntity form,
                                        @RequestParam(name = "imagenArchivo", required = false) MultipartFile imagenArchivo,
                                        @RequestParam(name = "imagenActual", required = false) String imagenActual) {
        guardarOActualizar(form, imagenArchivo, imagenActual, "empleado");
        return "redirect:/eventos";
    }

    private void guardarOActualizar(EventEntity form,
                                    MultipartFile imagenArchivo,
                                    String imagenActual,
                                    String creadoPor) {
        EventEntity entity = form.getId() != null
                ? eventRepo.findById(form.getId()).orElse(new EventEntity())
                : new EventEntity();

        entity.setTitulo(form.getTitulo());
        entity.setDescripcion(form.getDescripcion());
        entity.setCategoria(form.getCategoria());
        entity.setFecha(form.getFecha() != null ? form.getFecha() : LocalDate.now());
        entity.setUbicacion(form.getUbicacion());
        entity.setDestacado(form.isDestacado());

        String imagenUrl = imagenActual;
        if (imagenArchivo != null && !imagenArchivo.isEmpty()) {
            imagenUrl = guardarImagen(imagenArchivo);
        }
        entity.setImagenUrl(imagenUrl);

        if (entity.getId() == null) {
            entity.setCreadoEn(LocalDateTime.now());
            entity.setCreadoPor(creadoPor);
        }
        eventRepo.save(entity);
    }

    private String guardarImagen(MultipartFile file) {
        try {
            if (!Files.exists(imagesDir)) {
                Files.createDirectories(imagesDir);
            }
            String cleaned = file.getOriginalFilename() == null ? "evento" : file.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]", "_");
            String filename = LocalDateTime.now().toString().replaceAll("[:.]", "") + "_" + cleaned;
            Path target = imagesDir.resolve(filename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return "/images/" + filename;
        } catch (IOException e) {
            return null;
        }
    }
}
