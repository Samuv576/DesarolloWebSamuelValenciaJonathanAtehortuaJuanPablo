package com.Patinaje.V1.infrastructure.adapter.in.web;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.ClassJpaRepository;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.InstructorJpaRepository;

@Controller
@RequestMapping("/instructor")
public class InstructorPortalController {

    private final InstructorJpaRepository instructorRepo;
    private final ClassJpaRepository classRepo;

    public InstructorPortalController(InstructorJpaRepository instructorRepo,
                                      ClassJpaRepository classRepo) {
        this.instructorRepo = instructorRepo;
        this.classRepo = classRepo;
    }

    @GetMapping("/clases")
    public String misClases(Principal principal, Model model) {
        // demo: traer todas las clases (sin mapear usuario->instructor real)
        model.addAttribute("clases", classRepo.findAll());
        return "instructor/clases";
    }
}
