package com.Patinaje.V1.infrastructure.adapter.in.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.ClassJpaRepository;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.EnrollmentJpaRepository;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.ObservacionEntity;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.ObservacionJpaRepository;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.PaymentEntity;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.PaymentJpaRepository;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.StudentEntity;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.StudentJpaRepository;
import com.Patinaje.V1.domain.model.MedioPago;
import com.Patinaje.V1.domain.model.EstadoPago;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.security.Principal;
import java.util.List;
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
    private final ObservacionJpaRepository observacionRepo;

    public StudentPortalController(ClassJpaRepository classRepo,
                                   EnrollmentJpaRepository enrollmentRepo,
                                   PaymentJpaRepository paymentRepo,
                                   StudentJpaRepository studentRepo,
                                   ObservacionJpaRepository observacionRepo) {
        this.classRepo = classRepo;
        this.enrollmentRepo = enrollmentRepo;
        this.paymentRepo = paymentRepo;
        this.studentRepo = studentRepo;
        this.observacionRepo = observacionRepo;
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Dashboard estudiante", description = "Muestra datos del estudiante y clases disponibles.")
    public String dashboard(Model model, Principal principal) {
        resolveStudent(principal).ifPresent(est -> model.addAttribute("estudiante", est));
        model.addAttribute("clasesDisponibles", classRepo.findAll());
        model.addAttribute("sinEstudiante", resolveStudent(principal).isEmpty());
        return "estudiante/dashboard";
    }

    @GetMapping("/horario")
    @Operation(summary = "Horario", description = "Lista inscripciones filtradas por alumno.")
    public String horario(Model model, Principal principal) {
        var estudianteOpt = resolveStudent(principal);
        estudianteOpt.ifPresent(est -> model.addAttribute("estudiante", est));
        model.addAttribute("inscripciones", estudianteOpt.map(est -> enrollmentRepo.findByEstudianteId(est.getId())).orElseGet(List::of));
        model.addAttribute("sinEstudiante", estudianteOpt.isEmpty());
        return "estudiante/horario";
    }

    @GetMapping("/pagos")
    @Operation(summary = "Pagos", description = "Historial de pagos y formulario de nuevo pago.")
    public String pagos(Model model, Principal principal) {
        var estudianteOpt = resolveStudent(principal);
        estudianteOpt.ifPresent(est -> model.addAttribute("estudiante", est));
        model.addAttribute("pagos", estudianteOpt.map(est -> paymentRepo.findByEstudianteId(est.getId())).orElseGet(List::of));
        model.addAttribute("medios", MedioPago.values());
        model.addAttribute("sinEstudiante", estudianteOpt.isEmpty());
        return "estudiante/pagos";
    }

    @PostMapping("/pagos/pagar")
    @Operation(summary = "Registrar pago", description = "Registra pago pagado inmediatamente.")
    public String registrarPago(Principal principal,
                                @RequestParam BigDecimal monto,
                                @RequestParam MedioPago medio) {
        var estudianteOpt = resolveStudent(principal);
        if (estudianteOpt.isEmpty()) return "redirect:/estudiante/pagos?error=sin_estudiante";
        StudentEntity est = estudianteOpt.get();
        PaymentEntity pago = PaymentEntity.builder()
                .estudiante(est)
                .monto(monto)
                .fecha(LocalDate.now())
                .medioPago(medio)
                .estado(EstadoPago.PAGADO)
                .build();
        paymentRepo.save(pago);
        return "redirect:/estudiante/pagos";
    }

    @GetMapping("/progreso")
    @Operation(summary = "Progreso", description = "Vista de progreso (mock).")
    public String progreso(Model model, Principal principal) {
        var estudianteOpt = resolveStudent(principal);
        estudianteOpt.ifPresent(est -> {
            model.addAttribute("estudiante", est);
            model.addAttribute("observaciones", observacionRepo.findByEstudianteIdOrderByFechaDesc(est.getId()));
        });
        if (estudianteOpt.isEmpty()) {
            model.addAttribute("observaciones", List.of());
            model.addAttribute("sinEstudiante", true);
        } else {
            model.addAttribute("sinEstudiante", false);
        }
        return "estudiante/progreso";
    }

    @GetMapping("/contacto")
    @Operation(summary = "Contacto", description = "Canal de contacto del estudiante.")
    public String contacto(Model model, Principal principal) {
        resolveStudent(principal).ifPresent(est -> model.addAttribute("estudiante", est));
        return "estudiante/contacto";
    }

    private Optional<StudentEntity> resolveStudent(Principal principal) {
        if (principal == null) return Optional.empty();
        String username = principal.getName();
        String userLower = username.toLowerCase();
        return studentRepo.findAll().stream()
                .filter(s -> {
                    String correo = s.getCorreo() != null ? s.getCorreo().toLowerCase() : "";
                    String id = s.getIdentificacion() != null ? s.getIdentificacion().toLowerCase() : "";
                    String nombre = s.getNombre() != null ? s.getNombre().toLowerCase().replaceAll("\\s+", "") : "";
                    return userLower.equals(correo)
                            || userLower.equals(id)
                            || (!correo.isEmpty() && correo.startsWith(userLower)) // ejemplo: usuario "alumno" con correo "alumno@demo.com"
                            || userLower.equals(nombre);
                })
                .findFirst();
    }
}
