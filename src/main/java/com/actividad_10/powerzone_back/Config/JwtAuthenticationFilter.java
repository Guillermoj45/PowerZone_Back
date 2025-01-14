package com.actividad_10.powerzone_back.Config;
// TODO JJ MARICON
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Clase que se usa como filtros para ver si los tokens furulan
// Hereda de una clase que se llama cada vez que hay alguna llamada al backend

@Component // Anotación que hace que spring la gestione automáticamente
@RequiredArgsConstructor // genera un constructor con todos los @NonNull.
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // Constante donde guardamos el token
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
        }

        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUserEmail(jwt);
    }
}
