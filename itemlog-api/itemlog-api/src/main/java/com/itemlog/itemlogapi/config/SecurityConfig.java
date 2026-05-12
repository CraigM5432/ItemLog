package com.itemlog.itemlogapi.config;

import com.itemlog.itemlogapi.security.CustomAccessDeniedHandler;
import com.itemlog.itemlogapi.security.CustomAuthenticationEntryPoint;
import com.itemlog.itemlogapi.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Central Spring Security configuration for the REST API.
// Defines public routes, protected routes, stateless JWT authentication,
// and custom JSON error handling for authentication/authorisation failures.
@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    public SecurityConfig(
            JwtAuthFilter jwtAuthFilter,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
            CustomAccessDeniedHandler customAccessDeniedHandler
    ) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // JWT is used instead of server-side sessions, so CSRF/session state is disabled.
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Ensures authentication errors are returned as JSON rather than default HTML pages.
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )

                // Login, registration and health check are public; all other routes require JWT authentication.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/health", "/auth/**").permitAll()
                        .anyRequest().authenticated()
                )

                // Runs the JWT filter before Spring's default username/password filter.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // BCrypt is used to securely hash user passwords before database storage.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}