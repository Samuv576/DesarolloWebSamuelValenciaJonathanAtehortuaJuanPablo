package com.Patinaje.V1.infrastructure.adapter.in.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.ClassJpaRepository;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.InstructorJpaRepository;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.StudentJpaRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final StudentJpaRepository studentRepo;
    private final InstructorJpaRepository instructorRepo;
    private final ClassJpaRepository classRepo;

    public AdminController(StudentJpaRepository studentRepo,
                           InstructorJpaRepository instructorRepo,
                           ClassJpaRepository classRepo) {
        this.studentRepo = studentRepo;
        this.instructorRepo = instructorRepo;
        this.classRepo = classRepo;
    }

    @GetMapping("/estudiantes")
    public String estudiantes(Model model) {
        model.addAttribute("estudiantes", studentRepo.findAll());
        return "admin/estudiantes";
    }

    @GetMapping("/instructores")
    public String instructores(Model model) {
        model.addAttribute("instructores", instructorRepo.findAll());
        return "admin/instructores";
    }

    @GetMapping("/clases")
    public String clases(Model model) {
        model.addAttribute("clases", classRepo.findAll());
        return "admin/clases";
    }
}
