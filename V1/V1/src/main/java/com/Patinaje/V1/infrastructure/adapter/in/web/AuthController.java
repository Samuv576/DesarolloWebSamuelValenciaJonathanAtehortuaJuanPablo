package com.Patinaje.V1.infrastructure.adapter.in.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Patinaje.V1.domain.model.Role;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.UserEntity;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.UserJpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@Tag(name = "Autenticacion", description = "Registro y pagina de login")
public class AuthController {

    private final UserJpaRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserJpaRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    @Operation(summary = "Formulario de registro", description = "Devuelve la vista de registro de usuario.")
    public String showRegister(Model model) {
        return "register";
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar usuario", description = "Crea un usuario con rol ALUMNO si no existe el username.")
    public String doRegister(@RequestParam String username,
                             @RequestParam String password) {
        if (userRepo.findByUsername(username).isEmpty()) {
            userRepo.save(UserEntity.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .role(Role.ALUMNO)
                    .enabled(true)
                    .build());
            return "redirect:/login";
        }
        return "redirect:/register?error";
    }
}
