package com.actividad_10.powerzone_back.Email;

import com.actividad_10.powerzone_back.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private UserService userService;

    @Autowired
    private SendEmailClass sendEmailClass;

    @Async
    public void sendWelcomeEmail(String email) {
        if (userService.exists(email)) {
            sendEmailClass.sendEmail(
                    email,
                    "Bienvenid@ a PowerZone",
                    "Estamos encantados de que hayas confiado en nosotros como red social Fitness \uD83D\uDCAA\uD83D\uDCAA"
            );
        }
    }

    public void sendRecoveryEmail(String email, String recoveryCode) {
        sendEmailClass.sendEmail(
                email,
                "Recuperación de contraseña",
                "No compartas esto con nadie, tu código de recuperación es: " + recoveryCode
        );
    }
}