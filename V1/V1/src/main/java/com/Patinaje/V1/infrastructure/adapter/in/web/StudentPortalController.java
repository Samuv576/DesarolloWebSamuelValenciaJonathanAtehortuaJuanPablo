package com.Patinaje.V1.infrastructure.adapter.in.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.ClassJpaRepository;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.EnrollmentJpaRepository;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.PaymentEntity;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.PaymentJpaRepository;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.StudentJpaRepository;
import com.Patinaje.V1.domain.model.MedioPago;
import com.Patinaje.V1.domain.model.EstadoPago;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.security.Principal;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping("/estudiante")
@Tag(name = "Portal estudiante", description = "Dashboard, pagos, horario y progreso")
public class StudentPortalController {

    private final ClassJpaRepository classRepo;
    private final EnrollmentJpaRepository enrollmentRepo;
    private final PaymentJpaRepository paymentRepo;
    private final StudentJpaRepository studentRepo;

    public StudentPortalController(ClassJpaRepository classRepo,
                                   EnrollmentJpaRepository enrollmentRepo,
                                   PaymentJpaRepository paymentRepo,
                                   StudentJpaRepository studentRepo) {
        this.classRepo = classRepo;
        this.enrollmentRepo = enrollmentRepo;
        this.paymentRepo = paymentRepo;
        this.studentRepo = studentRepo;
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Dashboard estudiante", description = "Muestra datos del estudiante y clases disponibles.")
    public String dashboard(Model model, Principal principal) {
        resolveStudent(principal).ifPresent(est -> model.addAttribute("estudiante", est));
        model.addAttribute("clasesDisponibles", classRepo.findAll());
        return "estudiante/dashboard";
    }

    @GetMapping("/horario")
    @Operation(summary = "Horario", description = "Lista inscripciones (demo sin filtrar por alumno).")
    public String horario(Model model, Principal principal) {
        resolveStudent(principal).ifPresent(est -> model.addAttribute("estudiante", est));
        model.addAttribute("inscripciones", enrollmentRepo.findAll());
        return "estudiante/horario";
    }

    @GetMapping("/pagos")
    @Operation(summary = "Pagos", description = "Historial de pagos y formulario de nuevo pago.")
    public String pagos(Model model, Principal principal) {
        resolveStudent(principal).ifPresent(est -> model.addAttribute("estudiante", est));
        model.addAttribute("estudiantes", studentRepo.findAll());
        model.addAttribute("pagos", paymentRepo.findAll());
        model.addAttribute("medios", MedioPago.values());
        return "estudiante/pagos";
    }

    @PostMapping("/pagos/pagar")
    @Operation(summary = "Registrar pago", description = "Registra pago pagado inmediatamente.")
    public String registrarPago(@RequestParam Long estudianteId,
                                @RequestParam BigDecimal monto,
                                @RequestParam MedioPago medio) {
        studentRepo.findById(estudianteId).ifPresent(est -> {
            PaymentEntity pago = PaymentEntity.builder()
                    .estudiante(est)
                    .monto(monto)
                    .fecha(LocalDate.now())
                    .medioPago(medio)
                    .estado(EstadoPago.PAGADO)
                    .build();
            paymentRepo.save(pago);
        });
        return "redirect:/estudiante/pagos";
    }

    @GetMapping("/progreso")
    @Operation(summary = "Progreso", description = "Vista de progreso (mock).")
    public String progreso(Model model, Principal principal) {
        resolveStudent(principal).ifPresent(est -> model.addAttribute("estudiante", est));
        return "estudiante/progreso";
    }

    @GetMapping("/contacto")
    @Operation(summary = "Contacto", description = "Canal de contacto del estudiante.")
    public String contacto(Model model, Principal principal) {
        resolveStudent(principal).ifPresent(est -> model.addAttribute("estudiante", est));
        return "estudiante/contacto";
    }

    private Optional<com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.StudentEntity> resolveStudent(Principal principal) {
        if (principal == null) return Optional.empty();
        String username = principal.getName();
        return studentRepo.findAll().stream()
                .filter(s -> username.equalsIgnoreCase(s.getCorreo()) || username.equalsIgnoreCase(s.getIdentificacion()))
                .findFirst();
    }
}
