package com.Patinaje.V1.infrastructure.adapter.in.web;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.AttendanceEntity;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.AttendanceJpaRepository;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.ClassEntity;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.ClassJpaRepository;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.EnrollmentEntity;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.EnrollmentJpaRepository;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.InstructorEntity;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.InstructorJpaRepository;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.ObservacionEntity;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.ObservacionJpaRepository;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.StudentEntity;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.StudentJpaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping("/instructor")
@Tag(name = "Portal instructor", description = "Clases asignadas y registro de asistencia")
public class InstructorPortalController {

    private final InstructorJpaRepository instructorRepo;
    private final ClassJpaRepository classRepo;
    private final EnrollmentJpaRepository enrollmentRepo;
    private final StudentJpaRepository studentRepo;
    private final AttendanceJpaRepository attendanceRepo;
    private final ObservacionJpaRepository observacionRepo;

    public InstructorPortalController(InstructorJpaRepository instructorRepo,
                                      ClassJpaRepository classRepo,
                                      EnrollmentJpaRepository enrollmentRepo,
                                      StudentJpaRepository studentRepo,
                                      AttendanceJpaRepository attendanceRepo,
                                      ObservacionJpaRepository observacionRepo) {
        this.instructorRepo = instructorRepo;
        this.classRepo = classRepo;
        this.enrollmentRepo = enrollmentRepo;
        this.studentRepo = studentRepo;
        this.attendanceRepo = attendanceRepo;
        this.observacionRepo = observacionRepo;
    }

    @GetMapping("/clases")
    @Operation(summary = "Clases del instructor", description = "Lista clases (demo: no filtra por instructor logueado) y asistentes.")
    public String misClases(Principal principal, Model model) {
        var instructorOpt = resolveInstructor(principal);
        if (instructorOpt.isEmpty()) {
            model.addAttribute("clases", List.of());
            model.addAttribute("inscripciones", Map.of());
            model.addAttribute("presentesHoy", Map.of());
            model.addAttribute("hoy", LocalDate.now());
            model.addAttribute("sinInstructor", true);
            return "instructor/clases";
        }
        var instructor = instructorOpt.get();
        List<ClassEntity> clases = classRepo.findByInstructorId(instructor.getId());
        Map<Long, List<EnrollmentEntity>> inscripciones = clases.stream()
                .collect(Collectors.toMap(ClassEntity::getId, c -> enrollmentRepo.findByClaseId(c.getId())));
        LocalDate hoy = LocalDate.now();
        Map<Long, List<Long>> presentesHoy = clases.stream()
                .collect(Collectors.toMap(ClassEntity::getId,
                        c -> attendanceRepo.findByClaseIdAndFecha(c.getId(), hoy)
                                .stream()
                                .filter(AttendanceEntity::isPresente)
                                .map(a -> a.getEstudiante().getId())
                                .collect(Collectors.toList())));
        model.addAttribute("clases", clases);
        model.addAttribute("inscripciones", inscripciones);
        model.addAttribute("presentesHoy", presentesHoy);
        model.addAttribute("hoy", hoy);
        model.addAttribute("sinInstructor", false);
        return "instructor/clases";
    }

    @PostMapping("/asistencia")
    @Operation(summary = "Registrar asistencia", description = "Guarda asistencia del dia para una clase.")
    public String registrarAsistencia(@RequestParam Long claseId,
                                      @RequestParam(required = false, name = "presentes") List<Long> presentes) {
        LocalDate hoy = LocalDate.now();
        attendanceRepo.deleteByClaseIdAndFecha(claseId, hoy);
        List<Long> presentesIds = presentes != null ? presentes : List.of();
        enrollmentRepo.findByClaseId(claseId).forEach(enrollment -> {
            StudentEntity est = enrollment.getEstudiante();
            boolean presente = presentesIds.contains(est.getId());
            attendanceRepo.save(AttendanceEntity.builder()
                    .clase(enrollment.getClase())
                    .estudiante(est)
                    .fecha(hoy)
                    .presente(presente)
                    .build());
        });
        return "redirect:/instructor/clases";
    }

    @PostMapping("/observacion")
    @Operation(summary = "Registrar observacion de progreso", description = "Guarda nota y métricas para un estudiante")
    public String registrarObservacion(Principal principal,
                                       @RequestParam Long claseId,
                                       @RequestParam Long estudianteId,
                                       @RequestParam String nota) {
        var instructorOpt = resolveInstructor(principal);
        var estudianteOpt = studentRepo.findById(estudianteId);
        if (instructorOpt.isEmpty() || estudianteOpt.isEmpty()) {
            return "redirect:/instructor/clases";
        }
        ObservacionEntity obs = ObservacionEntity.builder()
                .estudiante(estudianteOpt.get())
                .instructor(instructorOpt.get())
                .fecha(LocalDate.now())
                .control(0)
                .velocidad(0)
                .equilibrio(0)
                .frenado(0)
                .nota(nota == null ? "" : nota)
                .build();
        observacionRepo.save(obs);
        return "redirect:/instructor/clases#clase-" + claseId;
    }

    private Optional<InstructorEntity> resolveInstructor(Principal principal) {
        if (principal == null) return Optional.empty();
        String user = principal.getName();
        String lower = user.toLowerCase();
        return instructorRepo.findAll().stream()
                .filter(i -> lower.equalsIgnoreCase(i.getIdentificacion()) ||
                        (i.getCorreo() != null && lower.equalsIgnoreCase(i.getCorreo())))
                .findFirst();
    }
}
