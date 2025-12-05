package com.Patinaje.V1.infrastructure.adapter.in.web;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.CommunityGroupEntity;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.CommunityGroupRepository;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.CommunityMessageEntity;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.CommunityMessageRepository;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.CommunityMembershipEntity;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.CommunityMembershipRepository;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.StudentJpaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping("/comunidad")
@Tag(name = "Comunidad", description = "Grupos, mensajes y miembros")
public class CommunityController {

    private final CommunityGroupRepository groupRepo;
    private final CommunityMessageRepository messageRepo;
    private final CommunityMembershipRepository membershipRepo;
    private final StudentJpaRepository studentRepo;

    public CommunityController(CommunityGroupRepository groupRepo,
                               CommunityMessageRepository messageRepo,
                               CommunityMembershipRepository membershipRepo,
                               StudentJpaRepository studentRepo) {
        this.groupRepo = groupRepo;
        this.messageRepo = messageRepo;
        this.membershipRepo = membershipRepo;
        this.studentRepo = studentRepo;
    }

    @GetMapping
    @Operation(summary = "Listar grupos", description = "Devuelve la vista con los grupos creados.")
    public String listar(Model model) {
        model.addAttribute("grupos", groupRepo.findAll());
        return "comunidad/lista";
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ver grupo", description = "Detalle del grupo con mensajes y miembros.")
    public String verGrupo(@PathVariable Long id, Model model) {
        var grupo = groupRepo.findById(id).orElse(null);
        if (grupo == null) return "redirect:/comunidad";
        model.addAttribute("grupo", grupo);
        model.addAttribute("mensajes", messageRepo.findByGroupIdOrderByCreadoEnAsc(id));
        model.addAttribute("miembros", membershipRepo.findByGroupId(id));
        model.addAttribute("estudiantes", studentRepo.findAll());
        return "comunidad/grupo";
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/nuevo")
    @Operation(summary = "Nuevo grupo", description = "Formulario para crear grupo.")
    public String nuevoGrupo(Model model) {
        model.addAttribute("grupo", new CommunityGroupEntity());
        return "comunidad/grupo_form";
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/guardar")
    @Operation(summary = "Guardar grupo", description = "Crea un grupo de comunidad.")
    public String guardarGrupo(CommunityGroupEntity grupo, Principal principal) {
        grupo.setCreadoEn(LocalDateTime.now());
        grupo.setCreadoPor(principal != null ? principal.getName() : "admin");
        groupRepo.save(grupo);
        return "redirect:/comunidad";
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/{id}/eliminar")
    @Operation(summary = "Eliminar grupo")
    public String eliminarGrupo(@PathVariable Long id) {
        membershipRepo.deleteAll(membershipRepo.findByGroupId(id));
        messageRepo.deleteAll(messageRepo.findByGroupIdOrderByCreadoEnAsc(id));
        groupRepo.deleteById(id);
        return "redirect:/comunidad";
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','INSTRUCTOR')")
    @PostMapping("/{id}/miembros/agregar")
    @Operation(summary = "Agregar miembro por username")
    public String agregarMiembro(@PathVariable Long id,
                                 @RequestParam String username,
                                 @RequestParam String rol) {
        var grupo = groupRepo.findById(id).orElse(null);
        if (grupo != null && !membershipRepo.existsByGroupIdAndUsername(id, username)) {
            membershipRepo.save(CommunityMembershipEntity.builder()
                    .group(grupo)
                    .username(username)
                    .rol(rol)
                    .build());
        }
        return "redirect:/comunidad/" + id;
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/{id}/miembros/agregar-estudiante")
    @Operation(summary = "Agregar miembro desde estudiante")
    public String agregarMiembroEstudiante(@PathVariable Long id,
                                           @RequestParam Long estudianteId) {
        var grupo = groupRepo.findById(id).orElse(null);
        var estudiante = studentRepo.findById(estudianteId).orElse(null);
        if (grupo != null && estudiante != null) {
            String username = estudiante.getCorreo() != null ? estudiante.getCorreo() : estudiante.getIdentificacion();
            if (!membershipRepo.existsByGroupIdAndUsername(id, username)) {
                membershipRepo.save(CommunityMembershipEntity.builder()
                        .group(grupo)
                        .username(username)
                        .rol("ALUMNO")
                        .build());
            }
        }
        return "redirect:/comunidad/" + id;
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','INSTRUCTOR')")
    @PostMapping("/{id}/miembros/{memberId}/eliminar")
    @Operation(summary = "Eliminar miembro")
    public String eliminarMiembro(@PathVariable Long id, @PathVariable Long memberId) {
        membershipRepo.deleteById(memberId);
        return "redirect:/comunidad/" + id;
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','INSTRUCTOR','ALUMNO')")
    @PostMapping("/{id}/mensajes")
    @Operation(summary = "Enviar mensaje")
    public String enviarMensaje(@PathVariable Long id,
                                @RequestParam String contenido,
                                Principal principal) {
        var grupo = groupRepo.findById(id).orElse(null);
        if (grupo != null && contenido != null && !contenido.isBlank()) {
            String autor = principal != null ? principal.getName() : "usuario";
            String rolAutor = "USUARIO";
            if (principal != null) {
                var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
                rolAutor = auth.getAuthorities().stream().findFirst().map(a -> a.getAuthority()).orElse("USUARIO");
            }
            messageRepo.save(CommunityMessageEntity.builder()
                    .group(grupo)
                    .contenido(contenido)
                    .autor(autor)
                    .rolAutor(rolAutor)
                    .creadoEn(LocalDateTime.now())
                    .build());
        }
        return "redirect:/comunidad/" + id;
    }
}
