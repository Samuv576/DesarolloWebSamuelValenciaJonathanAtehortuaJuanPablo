package com.Patinaje.V1.infrastructure.adapter.in.web;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.InstructorJpaRepository;
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

    public InstructorPortalController(InstructorJpaRepository instructorRepo,
                                      ClassJpaRepository classRepo,
                                      EnrollmentJpaRepository enrollmentRepo,
                                      StudentJpaRepository studentRepo,
                                      AttendanceJpaRepository attendanceRepo) {
        this.instructorRepo = instructorRepo;
        this.classRepo = classRepo;
        this.enrollmentRepo = enrollmentRepo;
        this.studentRepo = studentRepo;
        this.attendanceRepo = attendanceRepo;
    }

    @GetMapping("/clases")
    @Operation(summary = "Clases del instructor", description = "Lista clases (demo: no filtra por instructor logueado) y asistentes.")
    public String misClases(Principal principal, Model model) {
        List<ClassEntity> clases = classRepo.findAll();
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
}
