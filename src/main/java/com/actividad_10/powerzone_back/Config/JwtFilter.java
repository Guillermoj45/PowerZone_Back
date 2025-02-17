package com.actividad_10.powerzone_back.Config;

import com.actividad_10.powerzone_back.DTOs.TokenDto;
import com.actividad_10.powerzone_back.Entities.User;
import com.actividad_10.powerzone_back.Services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private UserService usuarioService;

    //Filtro que se va a seguir para ver si el token es correcto
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException
    {
        final String authHeader = request.getHeader("Authorization");

        // Si la ruta enviada por el http tiene /auth se salta el filtro

        String servletPath = request.getServletPath();
        if (servletPath.startsWith("/auth/forgot-password") || servletPath.startsWith("/api/forgot-password")
                || servletPath.startsWith("/api/auth/forgot-password") || servletPath.startsWith("/api/auth/reset-password")) {
            filterChain.doFilter(request, response);
            return;
        }


        if( request.getServletPath().contains("/auth")){
            filterChain.doFilter(request, response);
            return;
        }

        //Si el header es nulo o el token empieza por Bearer, me lo manda hacia atrás
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        //Quitamos la palabra Bearer...
        String token = authHeader.substring(7).trim();
        //Extraemos los datos del token
        TokenDto tokenDto = jwtService.extractTokenData(token);


        if (tokenDto != null && SecurityContextHolder.getContext().getAuthentication() == null){
            //Casteamos los datos del token a un objeto de tipo usuario
            User user = (User) usuarioService.loadUserByUsername(tokenDto.getEmail());

            //Si el usuario devuelto es nuelo o el token está expirado
            if (user != null && !jwtService.isExpired(token)){
                // Validamos el token
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword(),
                        user.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
