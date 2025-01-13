package com.actividad_10.powerzone_back.Config;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    AuthenticationConfiguration authenticationConfiguration;

    // Metodo que gestiona la cadena de filtros que usa el SecurityConfig
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //De momento desabilitamos tokens y todos los usuarios pueden visitar todas las páginas
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll() // Permite todas las solicitudes sin autenticación
                );
        return httpSecurity.build(); // Construimos el http
    }


    // Manager que gestiona la cadena de filtros
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Proveedor que le da los servicios al Manager
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder()); // Servicio de encriptado
        provider.setUserDetailsService(username -> { // Servicio de acesso a usuarios
            // Crea un usuario predeterminado
            return org.springframework.security.core.userdetails.User.builder()
                    .username("defaultUser")
                    .password(passwordEncoder().encode("defaultPassword"))
                    .roles("USER")
                    .build();
        });
        return provider;
    }


    //Método que me encripta las contraseñas
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
