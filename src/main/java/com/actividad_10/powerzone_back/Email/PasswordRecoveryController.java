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
            // Generar código de 6 dígitos
            String code = String.format("%06d", new java.util.Random().nextInt(999999));
            // Simular almacenamiento en DB
            userTokens.put(email, code);

            // Enviar correo
            emailService.sendEmail(email, "Recuperación de contraseña",
                    "No compartas esto con nadie, tu código de recuperación es: " + code);

            return "Correo de recuperación enviado a: " + email;
        }
        return "Visita la vida en 3d.";
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
            // Eliminar el código usado
            userTokens.remove(email);
            return "Contraseña actualizada correctamente.";
        }
        return "Código inválido o expirado.";
    }
}