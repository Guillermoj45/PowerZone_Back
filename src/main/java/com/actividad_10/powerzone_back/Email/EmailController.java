package com.actividad_10.powerzone_back.Email;

import com.actividad_10.powerzone_back.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    // Simulación de base de datos para tokens de recuperación
    private final Map<String, String> userTokens = new HashMap<>();

    @Async
    public void sendWelcomeEmail(String email) {
        if (userService.exists(email)) {
            emailService.sendWelcomeEmail(email);
        }
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) {
        if (userService.exists(email)) {
            // Generar código de 6 dígitos
            String recoveryCode = String.format("%06d", new java.util.Random().nextInt(999999));
            userTokens.put(email, recoveryCode); // Simular almacenamiento en DB

            // Enviar correo de recuperación
            emailService.sendRecoveryEmail(email, recoveryCode);

            return "Correo de recuperación enviado a: " + email;
        }
        return "El correo no está registrado.";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String code, @RequestParam String newPassword) {
        // Verificar si el código es válido
        String email = userTokens.entrySet().stream()
                .filter(entry -> entry.getValue().equals(code))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        if (email != null) {
            // Actualizar la contraseña del usuario
            userService.updatePassword(email, newPassword);
            userTokens.remove(email); // Eliminar el código usado
            return "Contraseña actualizada correctamente.";
        }
        return "Código inválido o expirado.";
    }
}