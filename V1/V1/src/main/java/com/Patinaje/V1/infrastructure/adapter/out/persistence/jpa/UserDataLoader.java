package com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa;

import com.Patinaje.V1.domain.model.EstadoInscripcion;
import com.Patinaje.V1.domain.model.EstadoPago;
import com.Patinaje.V1.domain.model.Genero;
import com.Patinaje.V1.domain.model.MedioPago;
import com.Patinaje.V1.domain.model.Nivel;
import com.Patinaje.V1.domain.model.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Configuration
public class UserDataLoader {

    @Bean
    CommandLineRunner seedUsers(UserJpaRepository userRepo,
                                StudentJpaRepository studentRepo,
                                InstructorJpaRepository instructorRepo,
                                ClassJpaRepository classRepo,
                                PaymentJpaRepository paymentRepo,
                                EnrollmentJpaRepository enrollmentRepo,
                                AttendanceJpaRepository attendanceRepo,
                                CommunityGroupRepository groupRepo,
                                CommunityMembershipRepository membershipRepo,
                                CommunityMessageRepository messageRepo,
                                PasswordEncoder encoder) {
        return args -> {
            var admin = ensureUser(userRepo, encoder, "admin", "admin123", Role.ADMINISTRADOR);
            ensureUser(userRepo, encoder, "admin2", "admin123", Role.ADMINISTRADOR);

            var instructorUser = ensureUser(userRepo, encoder, "instructor", "instructor123", Role.INSTRUCTOR);
            ensureUser(userRepo, encoder, "instructor2", "instructor123", Role.INSTRUCTOR);
            var instructor3User = ensureUser(userRepo, encoder, "instructor3", "instructor123", Role.INSTRUCTOR);

            var alumnoUser = ensureUser(userRepo, encoder, "alumno", "alumno123", Role.ALUMNO);
            ensureUser(userRepo, encoder, "alumno2", "alumno123", Role.ALUMNO);
            var alumno3User = ensureUser(userRepo, encoder, "alumno3", "alumno123", Role.ALUMNO);

            var instructor = ensureInstructor(instructorRepo,
                    "I123", "Instructor Demo", LocalDate.of(1990, 1, 1), Genero.MASCULINO,
                    "3100000000", "instructor@demo.com", "Santa Marta", "Velocidad");
            var instructor2 = ensureInstructor(instructorRepo,
                    "I456", "Mariana Tecnica", LocalDate.of(1992, 6, 15), Genero.FEMENINO,
                    "3200000000", "mariana@demo.com", "Barranquilla", "Tecnica y control");
            var instructor3 = ensureInstructor(instructorRepo,
                    "I789", "Carlos Urbano", LocalDate.of(1988, 3, 10), Genero.MASCULINO,
                    "3000000001", "carlos@demo.com", "Medellin", "Urbano y saltos");

            var claseBasica = ensureClass(classRepo, "Clase Basica", Nivel.BASICO, "LUNES,MARTES",
                    LocalTime.of(9, 0), LocalTime.of(10, 0), LocalDate.now(), LocalDate.now().plusMonths(1), 20, instructor);
            var claseIntermedia = ensureClass(classRepo, "Intermedio Tarde", Nivel.INTERMEDIO, "MIERCOLES,VIERNES",
                    LocalTime.of(17, 0), LocalTime.of(18, 30), LocalDate.now().plusDays(2), LocalDate.now().plusMonths(2), 18, instructor2);
            var claseAvanzada = ensureClass(classRepo, "Avanzado Nocturno", Nivel.AVANZADO, "JUEVES,SABADO",
                    LocalTime.of(19, 0), LocalTime.of(20, 30), LocalDate.now().plusDays(3), LocalDate.now().plusMonths(2), 15, instructor3);

            var alumnoDemo = ensureStudent(studentRepo,
                    "A123", "Alumno Demo", LocalDate.of(2005, 1, 1), Genero.MASCULINO,
                    "3000000000", "alumno@demo.com", "Santa Marta", Nivel.BASICO, MedioPago.TRANSFERENCIA, "Contacto Demo");
            var estudiante2 = ensureStudent(studentRepo,
                    "A456", "Laura Costa", LocalDate.of(2004, 5, 12), Genero.FEMENINO,
                    "3010000000", "laura@demo.com", "Barranquilla", Nivel.INTERMEDIO, MedioPago.TARJETA, "Ana Costa");
            var estudiante3 = ensureStudent(studentRepo,
                    "A789", "Mateo Ruedas", LocalDate.of(2002, 9, 20), Genero.MASCULINO,
                    "3020000000", "mateo@demo.com", "Medellin", Nivel.AVANZADO, MedioPago.EFECTIVO, "Carlos Ruedas");

            ensurePayment(paymentRepo, alumnoDemo, BigDecimal.valueOf(80000), LocalDate.now(), MedioPago.TRANSFERENCIA, EstadoPago.PENDIENTE);
            ensurePayment(paymentRepo, estudiante2, BigDecimal.valueOf(90000), LocalDate.now().minusDays(5), MedioPago.TARJETA, EstadoPago.PAGADO);
            ensurePayment(paymentRepo, estudiante3, BigDecimal.valueOf(95000), LocalDate.now().minusDays(10), MedioPago.EFECTIVO, EstadoPago.PAGADO);

            ensureEnrollment(enrollmentRepo, alumnoDemo, claseBasica, LocalDate.now().minusDays(2), EstadoInscripcion.ACTIVA);
            ensureEnrollment(enrollmentRepo, estudiante2, claseIntermedia, LocalDate.now().minusDays(1), EstadoInscripcion.ACTIVA);
            ensureEnrollment(enrollmentRepo, estudiante3, claseAvanzada, LocalDate.now().minusDays(1), EstadoInscripcion.ACTIVA);

            ensureAttendance(attendanceRepo, claseBasica, LocalDate.now(), alumnoDemo, true);
            ensureAttendance(attendanceRepo, claseBasica, LocalDate.now(), estudiante2, false);

            var beginnersGroup = ensureGroup(groupRepo,
                    "Principiantes Caribe",
                    "Grupo para resolver dudas de quienes inician en patinaje urbano.",
                    admin.getUsername(),
                    LocalDateTime.now().minusDays(2));
            var advancedGroup = ensureGroup(groupRepo,
                    "Avanzados Costa",
                    "Entrenamientos de velocidad y saltos para el equipo avanzado.",
                    instructorUser.getUsername(),
                    LocalDateTime.now().minusDays(1));

            ensureMembership(membershipRepo, beginnersGroup, alumnoUser.getUsername(), "ALUMNO");
            ensureMembership(membershipRepo, beginnersGroup, instructorUser.getUsername(), "INSTRUCTOR");
            ensureMembership(membershipRepo, advancedGroup, alumno3User.getUsername(), "ALUMNO");
            ensureMembership(membershipRepo, advancedGroup, instructor3User.getUsername(), "INSTRUCTOR");

            ensureMessage(messageRepo, beginnersGroup,
                    "Bienvenidos al grupo de principiantes, compartan avances y dudas.",
                    admin.getUsername(), "ADMINISTRADOR", LocalDateTime.now().minusDays(2));
            ensureMessage(messageRepo, beginnersGroup,
                    "Recuerden casco y protecciones para la sesion del lunes.",
                    instructorUser.getUsername(), "INSTRUCTOR", LocalDateTime.now().minusDays(1));
            ensureMessage(messageRepo, advancedGroup,
                    "Practica de saltos en parqueadero viernes 7pm.",
                    instructor3User.getUsername(), "INSTRUCTOR", LocalDateTime.now().minusHours(20));
            ensureMessage(messageRepo, advancedGroup,
                    "Yo llevo conos y rampas portatiles.",
                    alumno3User.getUsername(), "ALUMNO", LocalDateTime.now().minusHours(18));
        };
    }

    private UserEntity ensureUser(UserJpaRepository repo, PasswordEncoder encoder, String username, String password, Role role) {
        return repo.findByUsername(username).orElseGet(() -> repo.save(UserEntity.builder()
                .username(username)
                .password(encoder.encode(password))
                .role(role)
                .enabled(true)
                .build()));
    }

    private InstructorEntity ensureInstructor(InstructorJpaRepository repo,
                                              String identificacion,
                                              String nombre,
                                              LocalDate fechaNacimiento,
                                              Genero genero,
                                              String telefono,
                                              String correo,
                                              String direccion,
                                              String especialidad) {
        return repo.findByIdentificacion(identificacion).orElseGet(() -> repo.save(InstructorEntity.builder()
                .nombre(nombre)
                .identificacion(identificacion)
                .fechaNacimiento(fechaNacimiento)
                .genero(genero)
                .telefono(telefono)
                .correo(correo)
                .direccion(direccion)
                .especialidad(especialidad)
                .build()));
    }

    private StudentEntity ensureStudent(StudentJpaRepository repo,
                                        String identificacion,
                                        String nombre,
                                        LocalDate fechaNacimiento,
                                        Genero genero,
                                        String telefono,
                                        String correo,
                                        String direccion,
                                        Nivel nivel,
                                        MedioPago medioPago,
                                        String contactoEmergencia) {
        return repo.findByIdentificacion(identificacion).orElseGet(() -> repo.save(StudentEntity.builder()
                .nombre(nombre)
                .identificacion(identificacion)
                .fechaNacimiento(fechaNacimiento)
                .genero(genero)
                .telefono(telefono)
                .correo(correo)
                .direccion(direccion)
                .nivel(nivel)
                .medioPago(medioPago)
                .contactoEmergencia(contactoEmergencia)
                .build()));
    }

    private ClassEntity ensureClass(ClassJpaRepository repo,
                                    String nombre,
                                    Nivel nivel,
                                    String diasSemana,
                                    LocalTime horaInicio,
                                    LocalTime horaFin,
                                    LocalDate fechaInicio,
                                    LocalDate fechaFin,
                                    int cupo,
                                    InstructorEntity instructor) {
        return repo.findAll().stream()
                .filter(c -> nombre.equalsIgnoreCase(c.getNombre()))
                .findFirst()
                .orElseGet(() -> repo.save(ClassEntity.builder()
                        .nombre(nombre)
                        .nivel(nivel)
                        .diasSemana(diasSemana)
                        .horaInicio(horaInicio)
                        .horaFin(horaFin)
                        .fechaInicio(fechaInicio)
                        .fechaFin(fechaFin)
                        .cupo(cupo)
                        .instructor(instructor)
                        .build()));
    }

    private void ensurePayment(PaymentJpaRepository repo,
                               StudentEntity estudiante,
                               BigDecimal monto,
                               LocalDate fecha,
                               MedioPago medioPago,
                               EstadoPago estado) {
        boolean exists = repo.findByEstudianteId(estudiante.getId()).stream()
                .anyMatch(p -> p.getMonto().compareTo(monto) == 0 && p.getFecha().equals(fecha));
        if (!exists) {
            repo.save(PaymentEntity.builder()
                    .estudiante(estudiante)
                    .monto(monto)
                    .fecha(fecha)
                    .medioPago(medioPago)
                    .estado(estado)
                    .build());
        }
    }

    private void ensureEnrollment(EnrollmentJpaRepository repo,
                                  StudentEntity estudiante,
                                  ClassEntity clase,
                                  LocalDate fecha,
                                  EstadoInscripcion estado) {
        if (!repo.existsByEstudianteIdAndClaseId(estudiante.getId(), clase.getId())) {
            repo.save(EnrollmentEntity.builder()
                    .estudiante(estudiante)
                    .clase(clase)
                    .fecha(fecha.atStartOfDay())
                    .estado(estado)
                    .build());
        }
    }

    private void ensureAttendance(AttendanceJpaRepository repo,
                                  ClassEntity clase,
                                  LocalDate fecha,
                                  StudentEntity estudiante,
                                  boolean presente) {
        boolean exists = repo.findByClaseIdAndFecha(clase.getId(), fecha).stream()
                .anyMatch(a -> a.getEstudiante().getId().equals(estudiante.getId()));
        if (!exists) {
            repo.save(AttendanceEntity.builder()
                    .clase(clase)
                    .estudiante(estudiante)
                    .fecha(fecha)
                    .presente(presente)
                    .build());
        }
    }

    private CommunityGroupEntity ensureGroup(CommunityGroupRepository repo,
                                             String nombre,
                                             String descripcion,
                                             String creadoPor,
                                             LocalDateTime creadoEn) {
        return repo.findAll().stream()
                .filter(g -> nombre.equalsIgnoreCase(g.getNombre()))
                .findFirst()
                .orElseGet(() -> repo.save(CommunityGroupEntity.builder()
                        .nombre(nombre)
                        .descripcion(descripcion)
                        .creadoPor(creadoPor)
                        .creadoEn(creadoEn)
                        .build()));
    }

    private void ensureMembership(CommunityMembershipRepository repo,
                                  CommunityGroupEntity group,
                                  String username,
                                  String rol) {
        if (!repo.existsByGroupIdAndUsername(group.getId(), username)) {
            repo.save(CommunityMembershipEntity.builder()
                    .group(group)
                    .username(username)
                    .rol(rol)
                    .build());
        }
    }

    private void ensureMessage(CommunityMessageRepository repo,
                               CommunityGroupEntity group,
                               String contenido,
                               String autor,
                               String rolAutor,
                               LocalDateTime creadoEn) {
        boolean exists = repo.findByGroupIdOrderByCreadoEnAsc(group.getId()).stream()
                .anyMatch(m -> m.getAutor().equalsIgnoreCase(autor) && m.getContenido().equals(contenido));
        if (!exists) {
            repo.save(CommunityMessageEntity.builder()
                    .group(group)
                    .contenido(contenido)
                    .autor(autor)
                    .rolAutor(rolAutor)
                    .creadoEn(creadoEn)
                    .build());
        }
    }
}
