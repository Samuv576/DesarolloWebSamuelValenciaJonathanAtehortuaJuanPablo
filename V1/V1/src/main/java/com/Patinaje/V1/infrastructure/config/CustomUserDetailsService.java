package com.Patinaje.V1.infrastructure.config;

import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.UserEntity;
import com.Patinaje.V1.infrastructure.adapter.out.persistence.jpa.UserJpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;

import com.Patinaje.V1.shared.security.LoginAttemptService;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserJpaRepository userRepo;
    private final LoginAttemptService attemptService;

    public CustomUserDetailsService(UserJpaRepository userRepo,
                                    LoginAttemptService attemptService) {
        this.userRepo = userRepo;
        this.attemptService = attemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (attemptService.isBlocked(username)) {
            throw new LockedException("Usuario bloqueado por intentos fallidos");
        }
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .disabled(!user.isEnabled())
                .build();
    }
}
