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
                    "\uD83C\uDFCB\uFE0F\u200D♂\uFE0F ¡Bienvenido a PowerZone! \uD83C\uDF4F\n" +
                            "\n" +
                            "¡Nos emociona tenerte aquí! \uD83D\uDE80 PowerZone es la red social diseñada para los apasionados del fitness y la vida saludable. Aquí podrás:\n" +
                            "\n" +
                            "✅ Compartir tu progreso con fotos y publicaciones.\n" +
                            "\uD83E\uDD16 Preguntar a nuestra IA sobre entrenamientos, nutrición y bienestar.\n" +
                            "\uD83D\uDCAC Chatear con otros usuarios y hacer comunidad.\n" +
                            "\uD83C\uDFC6 Descubrir nuevos retos y mantenerte motivado.\n" +
                            "\n" +
                            "Estás en el lugar perfecto para alcanzar tus objetivos y conectar con personas que comparten tu estilo de vida. ¡Empieza explorando y haz que cada día cuente! \uD83D\uDCAA\uD83D\uDD25\n" +
                            "\n" +
                            "\uD83D\uDCF2 ¿Listo para la acción? Comparte tu primera publicación o únete a la conversación.\n" +
                            "\n" +
                            "#PowerZone #Fitness #Salud #Comunidad \uD83D\uDCAA\uD83D\uDCAA"
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