package com.actividad_10.powerzone_back.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import com.actividad_10.powerzone_back.Services.UserService;

@RestController
@RequestMapping("/api/auth")
public class PasswordRecoveryController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    // Simulación de base de datos
    private Map<String, String> userTokens = new HashMap<>();

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) {
        if (userService.exists(email)) {
            // Generar token único
            String token = TokenGenerator.generateToken();
            // Simular almacenamiento en DB
            userTokens.put(email, token);

            // Enlace de recuperación
            String resetLink = "https://www.youtube.com/watch?v=WQbc2izxuVg" + token;

            // Enviar correo
            emailService.sendEmail(email, "Recuperación de contraseña",
                    "No compartas esto con nadie, tu contraseña es: " + resetLink);

            return "Correo de recuperación enviado a: " + email;
        }
        return "Visita la vida en 3d.";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        // Buscar email por token
        String email = userTokens.entrySet().stream()
                .filter(entry -> entry.getValue().equals(token))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        if (email == null) {
            return "Token inválido o expirado.";
        }

        // Actualizar contraseña (simulación)
        // Aquí deberías actualizar la contraseña en la base de datos
        return "Contraseña actualizada correctamente para el usuario: " + email;
    }
}