package com.actividad_10.powerzone_back.Controllers;

import com.actividad_10.powerzone_back.Config.JwtService;
import com.actividad_10.powerzone_back.DTOs.ChatMessage;
import com.actividad_10.powerzone_back.DTOs.Notificaciones.BaseNotification;
import com.actividad_10.powerzone_back.DTOs.Notificaciones.MegaNotificacion;
import com.actividad_10.powerzone_back.Entities.Notification;
import com.actividad_10.powerzone_back.Services.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@AllArgsConstructor
public class NotificationController {
    NotificationService notificationService;
    JwtService jwtService;

    @GetMapping
    public List<MegaNotificacion> getNotification(@RequestHeader("Authorization") String token, @RequestParam(value = "offset", defaultValue = "0") long offaset){


        return notificationService.getNotification(token, offaset);
    }

    // Manejar mensajes enviados por los clientes
    @MessageMapping("/roomNotification/{roomId}") // Los clientes envían mensajes a /app/chat
    @SendTo("/topic/roomNotification/{roomId}") // Los mensajes se envían a los suscriptores de /topic/messages
    public ChatMessage send(@DestinationVariable String roomId, ChatMessage message) {
        // Aquí puedes guardar el mensaje en la base de datos si es necesario
        // message.setTimestamp(System.currentTimeMillis());

        System.out.println("Mensaje recibido en el servidor: " + message);
        return message; // El mensaje se retransmite a todos los suscriptores
    }
}
