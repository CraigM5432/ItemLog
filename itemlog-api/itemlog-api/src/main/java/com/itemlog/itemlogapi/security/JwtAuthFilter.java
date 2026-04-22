package com.itemlog.itemlogapi.security;

import com.itemlog.itemlogapi.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepo;

    public JwtAuthFilter(JwtService jwtService, UserRepository userRepo) {
        this.jwtService = jwtService;
        this.userRepo = userRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // No token → continue (Spring will handle unauthorized)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String token = authHeader.substring(7);

            String username = jwtService.extractUsername(token);
            Integer userId = jwtService.extractUserId(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (userRepo.findByUsername(username).isPresent()) {

                    var authentication = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_USER"))
                    );

                    // store userId for later use
                    authentication.setDetails(Map.of("userId", userId));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

        } catch (ExpiredJwtException ex) {
            // Token expired → clear context, let Spring return 401
            SecurityContextHolder.clearContext();

        } catch (JwtException | IllegalArgumentException ex) {
            // Invalid token → clear context, let Spring return 401
            SecurityContextHolder.clearContext();
        }

        // Continue filter chain (important)
        filterChain.doFilter(request, response);
    }
}