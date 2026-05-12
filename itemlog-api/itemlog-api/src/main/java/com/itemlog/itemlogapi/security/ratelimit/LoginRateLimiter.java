package com.itemlog.itemlogapi.security.ratelimit;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Simple in-memory rate limiter used to reduce brute-force login attempts.
// For production scaling, this could be replaced with Redis or another shared store.
@Component
public class LoginRateLimiter {

    private static final int MAX_REQUESTS = 5;
    private static final long WINDOW_SECONDS = 60;

    private final Map<String, Attempt> attempts = new ConcurrentHashMap<>();

    public boolean isAllowed(String key) {
        Attempt attempt = attempts.getOrDefault(key, new Attempt(0, Instant.now()));

        if (Instant.now().isAfter(attempt.windowStart.plusSeconds(WINDOW_SECONDS))) {
            attempt = new Attempt(0, Instant.now());
        }

        attempt.count++;
        attempts.put(key, attempt);

        return attempt.count <= MAX_REQUESTS;
    }

    private static class Attempt {
        int count;
        Instant windowStart;

        Attempt(int count, Instant windowStart) {
            this.count = count;
            this.windowStart = windowStart;
        }
    }
}