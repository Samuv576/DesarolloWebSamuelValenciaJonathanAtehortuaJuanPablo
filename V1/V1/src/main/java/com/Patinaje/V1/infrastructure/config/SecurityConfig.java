package com.Patinaje.V1.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.Patinaje.V1.shared.security.LoginAttemptService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationSuccessHandler successHandler,
                                                   AuthenticationFailureHandler failureHandler) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/home", "/index", "/institucional", "/institucional/**",
                                 "/style.css", "/images/**", "/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg",
                                 "/login", "/register", "/tienda", "/producto/**", "/clases", "/contactar", "/sobre-nosotros",
                                 "/politica-privacidad", "/preguntas-frecuentes", "/terminos", "/aviso_legal",
                                 "/instructores", "/galeria", "/eventos", "/cart", "/cart/**", "/checkout", "/institucional").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/empleado/**").hasAnyRole("EMPLEADO","ADMINISTRADOR")
                .requestMatchers("/instructor/**").hasRole("INSTRUCTOR")
                .requestMatchers("/estudiante/**").hasRole("ALUMNO")
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout.logoutSuccessUrl("/"))
            ;
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(LoginAttemptService attemptService) {
        return (request, response, authentication) -> {
            attemptService.loginSucceeded(authentication.getName());
            response.sendRedirect("/");
        };
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(LoginAttemptService attemptService) {
        return (request, response, exception) -> {
            String username = request.getParameter("username");
            attemptService.loginFailed(username);
            boolean blocked = attemptService.isBlocked(username);
            response.sendRedirect("/login?error=" + (blocked ? "blocked" : "bad"));
        };
    }
}
