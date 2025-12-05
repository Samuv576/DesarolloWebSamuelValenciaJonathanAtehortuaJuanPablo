package com.Patinaje.V1.shared.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 3;
    private final Map<String, Integer> attempts = new ConcurrentHashMap<>();

    public void loginSucceeded(String username) {
        if (username != null) {
            attempts.remove(username);
        }
    }

    public void loginFailed(String username) {
        if (username == null) return;
        attempts.merge(username, 1, Integer::sum);
    }

    public boolean isBlocked(String username) {
        if (username == null) return false;
        return attempts.getOrDefault(username, 0) >= MAX_ATTEMPTS;
    }

    public int attempts(String username) {
        return attempts.getOrDefault(username, 0);
    }
}
