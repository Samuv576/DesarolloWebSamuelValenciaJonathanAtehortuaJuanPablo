package com.Patinaje.V1.infrastructure.adapter.in.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.ClassJpaRepository;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.EnrollmentJpaRepository;

@Controller
@RequestMapping("/estudiante")
public class StudentPortalController {

    private final ClassJpaRepository classRepo;
    private final EnrollmentJpaRepository enrollmentRepo;

    public StudentPortalController(ClassJpaRepository classRepo,
                                   EnrollmentJpaRepository enrollmentRepo) {
        this.classRepo = classRepo;
        this.enrollmentRepo = enrollmentRepo;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("clasesDisponibles", classRepo.findAll());
        return "estudiante/dashboard";
    }

    @GetMapping("/horario")
    public String horario(Model model) {
        model.addAttribute("inscripciones", enrollmentRepo.findAll());
        return "estudiante/horario";
    }

    @GetMapping("/pagos")
    public String pagos() {
        return "estudiante/pagos";
    }

    @GetMapping("/progreso")
    public String progreso() {
        return "estudiante/progreso";
    }
}
