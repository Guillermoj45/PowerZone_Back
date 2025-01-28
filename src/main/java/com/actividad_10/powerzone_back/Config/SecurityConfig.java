package com.actividad_10.powerzone_back.Config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilterChain;

    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // Deshabilitar CSRF y CORS (si es necesario)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)

                // Configuración de rutas y permisos
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("**").permitAll()
                        .anyRequest().authenticated() // Autenticar cualquier otra ruta
                )

                // Configurar autenticación y filtros
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilterChain, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}
