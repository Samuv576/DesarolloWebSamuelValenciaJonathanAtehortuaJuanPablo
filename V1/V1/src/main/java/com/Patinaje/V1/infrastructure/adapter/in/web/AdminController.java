package com.Patinaje.V1.infrastructure.adapter.in.web;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Patinaje.V1.domain.model.EstadoPago;
import com.Patinaje.V1.domain.model.EstadoInscripcion;
import com.Patinaje.V1.domain.model.MedioPago;
import com.Patinaje.V1.domain.model.Nivel;
import com.Patinaje.V1.domain.model.Role;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.UserEntity;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.UserJpaRepository;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.ClassEntity;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.ClassJpaRepository;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.EnrollmentEntity;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.EnrollmentJpaRepository;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.InstructorEntity;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.InstructorJpaRepository;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.PaymentEntity;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.PaymentJpaRepository;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.StudentEntity;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.StudentJpaRepository;
import com.Patinaje.V1.shared.security.LoginAttemptService;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping("/admin")
@Tag(name = "Administracion", description = "CRUD de estudiantes, instructores, clases, pagos y usuarios")
public class AdminController {

    private final StudentJpaRepository studentRepo;
    private final InstructorJpaRepository instructorRepo;
    private final ClassJpaRepository classRepo;
    private final EnrollmentJpaRepository enrollmentRepo;
    private final PaymentJpaRepository paymentRepo;
    private final UserJpaRepository userRepo;
    private final LoginAttemptService attemptService;
    private final PasswordEncoder passwordEncoder;

    public AdminController(StudentJpaRepository studentRepo,
                           InstructorJpaRepository instructorRepo,
                           ClassJpaRepository classRepo,
                           EnrollmentJpaRepository enrollmentRepo,
                           PaymentJpaRepository paymentRepo,
                           UserJpaRepository userRepo,
                           LoginAttemptService attemptService,
                           PasswordEncoder passwordEncoder) {
        this.studentRepo = studentRepo;
        this.instructorRepo = instructorRepo;
        this.classRepo = classRepo;
        this.enrollmentRepo = enrollmentRepo;
        this.paymentRepo = paymentRepo;
        this.userRepo = userRepo;
        this.attemptService = attemptService;
        this.passwordEncoder = passwordEncoder;
    }

    // Estudiantes
    @GetMapping("/estudiantes")
    @Operation(summary = "Listar estudiantes")
    public String estudiantes(Model model) {
        model.addAttribute("estudiantes", studentRepo.findAll());
        model.addAttribute("clases", classRepo.findAll());
        return "admin/estudiantes";
    }

    @GetMapping("/estudiantes/new")
    @Operation(summary = "Formulario nuevo estudiante")
    public String nuevoEstudiante(Model model) {
        model.addAttribute("estudiante", new StudentEntity());
        return "admin/estudiante_form";
    }

    @GetMapping("/estudiantes/{id}/edit")
    @Operation(summary = "Editar estudiante")
    public String editarEstudiante(@PathVariable Long id, Model model) {
        StudentEntity estudiante = studentRepo.findById(id).orElse(null);
        if (estudiante == null) return "redirect:/admin/estudiantes";
        model.addAttribute("estudiante", estudiante);
        return "admin/estudiante_form";
    }

    @PostMapping("/estudiantes/save")
    @Operation(summary = "Guardar estudiante")
    public String guardarEstudiante(StudentEntity estudiante) {
        studentRepo.save(estudiante);
        return "redirect:/admin/estudiantes";
    }

    @PostMapping("/estudiantes/{id}/delete")
    @Operation(summary = "Eliminar estudiante")
    public String eliminarEstudiante(@PathVariable Long id) {
        studentRepo.deleteById(id);
        return "redirect:/admin/estudiantes";
    }

    @PostMapping("/estudiantes/{id}/inscribir")
    @Operation(summary = "Inscribir estudiante en clase")
    public String inscribirEnClase(@PathVariable Long id, @RequestParam Long claseId) {
        var estudiante = studentRepo.findById(id).orElse(null);
        var clase = classRepo.findById(claseId).orElse(null);
        if (estudiante != null && clase != null && !enrollmentRepo.existsByEstudianteIdAndClaseId(id, claseId)) {
            enrollmentRepo.save(EnrollmentEntity.builder()
                    .estudiante(estudiante)
                    .clase(clase)
                    .fecha(LocalDate.now().atStartOfDay())
                    .estado(EstadoInscripcion.ACTIVA)
                    .build());
        }
        return "redirect:/admin/estudiantes";
    }

    // Instructores
    @GetMapping("/instructores")
    @Operation(summary = "Listar instructores")
    public String instructores(Model model) {
        model.addAttribute("instructores", instructorRepo.findAll());
        return "admin/instructores";
    }

    @GetMapping("/instructores/new")
    @Operation(summary = "Formulario nuevo instructor")
    public String nuevoInstructor(Model model) {
        model.addAttribute("instructor", new InstructorEntity());
        return "admin/instructor_form";
    }

    @GetMapping("/instructores/{id}/edit")
    @Operation(summary = "Editar instructor")
    public String editarInstructor(@PathVariable Long id, Model model) {
        InstructorEntity instructor = instructorRepo.findById(id).orElse(null);
        if (instructor == null) return "redirect:/admin/instructores";
        model.addAttribute("instructor", instructor);
        return "admin/instructor_form";
    }

    @PostMapping("/instructores/save")
    @Operation(summary = "Guardar instructor")
    public String guardarInstructor(InstructorEntity instructor) {
        instructorRepo.save(instructor);
        return "redirect:/admin/instructores";
    }

    @PostMapping("/instructores/{id}/delete")
    @Operation(summary = "Eliminar instructor")
    public String eliminarInstructor(@PathVariable Long id) {
        instructorRepo.deleteById(id);
        return "redirect:/admin/instructores";
    }

    // Clases / Horarios
    @GetMapping({"/clases", "/horarios"})
    @Operation(summary = "Listar clases/horarios")
    public String horarios(Model model) {
        List<HorarioView> clasesView = classRepo.findAll().stream()
                .map(this::mapToView)
                .collect(Collectors.toList());
        long diasConClases = classRepo.findAll().stream()
                .map(ClassEntity::getDiasSemana)
                .filter(s -> s != null && !s.isBlank())
                .flatMap(s -> List.of(s.split(",")).stream())
                .map(String::trim)
                .filter(d -> !d.isBlank())
                .distinct()
                .count();
        model.addAttribute("clases", clasesView);
        model.addAttribute("diasConClases", diasConClases);
        model.addAttribute("niveles", Nivel.values());
        model.addAttribute("daysOfWeek", DayOfWeek.values());
        model.addAttribute("instructores", instructorRepo.findAll());
        return "admin/horarios";
    }

    @GetMapping("/clases/new")
    @Operation(summary = "Formulario nueva clase")
    public String nuevaClase(Model model) {
        model.addAttribute("clase", new ClassEntity());
        model.addAttribute("niveles", Nivel.values());
        model.addAttribute("daysOfWeek", DayOfWeek.values());
        model.addAttribute("instructores", instructorRepo.findAll());
        return "admin/clase_form";
    }

    @GetMapping("/clases/{id}/edit")
    @Operation(summary = "Editar clase")
    public String editarClase(@PathVariable Long id, Model model) {
        ClassEntity clase = classRepo.findById(id).orElse(null);
        if (clase == null) return "redirect:/admin/clases";
        model.addAttribute("clase", clase);
        model.addAttribute("niveles", Nivel.values());
        model.addAttribute("daysOfWeek", DayOfWeek.values());
        model.addAttribute("instructores", instructorRepo.findAll());
        return "admin/clase_form";
    }

    @PostMapping("/clases/save")
    @Operation(summary = "Guardar clase")
    public String guardarClase(@RequestParam Long instructorId,
                               @RequestParam(required = false, name = "diasSeleccionados") List<String> diasSeleccionados,
                               ClassEntity clase) {
        instructorRepo.findById(instructorId).ifPresent(clase::setInstructor);
        if (diasSeleccionados != null) {
            Set<String> unicos = new LinkedHashSet<>();
            for (String dia : diasSeleccionados) {
                if (dia != null && !dia.isBlank()) {
                    unicos.add(dia.trim().toUpperCase());
                }
            }
            clase.setDiasSemana(String.join(",", unicos));
        } else {
            clase.setDiasSemana(null);
        }
        classRepo.save(clase);
        return "redirect:/admin/clases";
    }

    @PostMapping("/clases/{id}/delete")
    @Operation(summary = "Eliminar clase")
    public String eliminarClase(@PathVariable Long id) {
        classRepo.deleteById(id);
        return "redirect:/admin/clases";
    }

    // Pagos
    @GetMapping("/pagos")
    @Operation(summary = "Listar pagos")
    public String pagos(Model model) {
        model.addAttribute("pagos", paymentRepo.findAll());
        model.addAttribute("estudiantes", studentRepo.findAll());
        model.addAttribute("medios", MedioPago.values());
        model.addAttribute("estados", EstadoPago.values());
        return "admin/pagos";
    }

    @GetMapping("/pagos/new")
    @Operation(summary = "Formulario nuevo pago")
    public String nuevoPago(Model model) {
        model.addAttribute("pago", new PaymentEntity());
        model.addAttribute("estudiantes", studentRepo.findAll());
        model.addAttribute("medios", MedioPago.values());
        model.addAttribute("estados", EstadoPago.values());
        return "admin/pago_form";
    }

    @GetMapping("/pagos/{id}/edit")
    @Operation(summary = "Editar pago")
    public String editarPago(@PathVariable Long id, Model model) {
        PaymentEntity pago = paymentRepo.findById(id).orElse(null);
        if (pago == null) return "redirect:/admin/pagos";
        model.addAttribute("pago", pago);
        model.addAttribute("estudiantes", studentRepo.findAll());
        model.addAttribute("medios", MedioPago.values());
        model.addAttribute("estados", EstadoPago.values());
        return "admin/pago_form";
    }

    @PostMapping("/pagos/save")
    @Operation(summary = "Guardar pago")
    public String guardarPago(@RequestParam Long estudianteId,
                              PaymentEntity pago) {
        studentRepo.findById(estudianteId).ifPresent(pago::setEstudiante);
        paymentRepo.save(pago);
        return "redirect:/admin/pagos";
    }

    @PostMapping("/pagos/{id}/estado")
    @Operation(summary = "Actualizar estado de pago")
    public String actualizarEstadoPago(@PathVariable Long id, @RequestParam("estado") EstadoPago estado) {
        paymentRepo.findById(id).ifPresent(p -> {
            p.setEstado(estado);
            paymentRepo.save(p);
        });
        return "redirect:/admin/pagos";
    }

    @PostMapping("/pagos/{id}/delete")
    @Operation(summary = "Eliminar pago")
    public String eliminarPago(@PathVariable Long id) {
        paymentRepo.deleteById(id);
        return "redirect:/admin/pagos";
    }

    // Usuarios
    @GetMapping("/usuarios")
    @Operation(summary = "Listar usuarios y bloqueos")
    public String usuarios(Model model) {
        List<UserEntity> usuarios = userRepo.findAll();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("attempts", usuarios.stream()
                .collect(Collectors.toMap(UserEntity::getUsername, u -> attemptService.attempts(u.getUsername()))));
        model.addAttribute("roles", Role.values());
        return "admin/usuarios";
    }

    @PostMapping("/usuarios/{id}/desbloquear")
    @Operation(summary = "Desbloquear usuario")
    public String desbloquear(@PathVariable Long id) {
        userRepo.findById(id).ifPresent(user -> {
            user.setEnabled(true);
            userRepo.save(user);
            attemptService.loginSucceeded(user.getUsername());
        });
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuarios/save")
    @Operation(summary = "Crear usuario")
    public String crearUsuario(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam Role role,
                               @RequestParam(defaultValue = "true") boolean enabled) {
        if (userRepo.findByUsername(username).isEmpty()) {
            userRepo.save(UserEntity.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .role(role)
                    .enabled(enabled)
                    .build());
        }
        return "redirect:/admin/usuarios";
    }

    private HorarioView mapToView(ClassEntity clase) {
        DateTimeFormatter fechaFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter horaFmt = DateTimeFormatter.ofPattern("HH:mm");
        String dias = clase.getDiasSemana() != null ? toSpanishDays(clase.getDiasSemana()) : "Sin dias";
        String horario = (clase.getHoraInicio() != null && clase.getHoraFin() != null)
                ? clase.getHoraInicio().format(horaFmt) + " - " + clase.getHoraFin().format(horaFmt)
                : "Sin horario definido";
        String inicio = clase.getFechaInicio() != null ? clase.getFechaInicio().format(fechaFmt) : "-";
        String fin = clase.getFechaFin() != null ? clase.getFechaFin().format(fechaFmt) : "-";
        String instructorNombre = clase.getInstructor() != null ? clase.getInstructor().getNombre() : "Sin asignar";
        return new HorarioView(clase.getId(), clase.getNombre(), clase.getNivel(), dias, horario, instructorNombre,
                clase.getCupo(), inicio, fin);
    }

    private String toSpanishDays(String diasSemana) {
        return List.of(diasSemana.split(",")).stream()
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(s -> {
                    try {
                        return DayOfWeek.valueOf(s.toUpperCase(Locale.ROOT))
                                .getDisplayName(java.time.format.TextStyle.FULL, new Locale("es", "ES"));
                    } catch (Exception e) {
                        return s;
                    }
                })
                .distinct()
                .collect(Collectors.joining(", "));
    }

    private static class HorarioView {
        private final Long id;
        private final String nombre;
        private final Nivel nivel;
        private final String dias;
        private final String horario;
        private final String instructor;
        private final Integer cupo;
        private final String fechaInicio;
        private final String fechaFin;

        HorarioView(Long id, String nombre, Nivel nivel, String dias, String horario, String instructor, Integer cupo, String fechaInicio, String fechaFin) {
            this.id = id;
            this.nombre = nombre;
            this.nivel = nivel;
            this.dias = dias;
            this.horario = horario;
            this.instructor = instructor;
            this.cupo = cupo;
            this.fechaInicio = fechaInicio;
            this.fechaFin = fechaFin;
        }

        public Long getId() { return id; }
        public String getNombre() { return nombre; }
        public Nivel getNivel() { return nivel; }
        public String getDias() { return dias; }
        public String getHorario() { return horario; }
        public String getInstructor() { return instructor; }
        public Integer getCupo() { return cupo; }
        public String getFechaInicio() { return fechaInicio; }
        public String getFechaFin() { return fechaFin; }
    }
}
