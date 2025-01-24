package com.actividad_10.powerzone_back.Controllers;

import com.actividad_10.powerzone_back.DTOs.ChatMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequestMapping("/api/messages")
public class messageController {

    // Manejar mensajes enviados por los clientes
    @MessageMapping("/chat") // Los clientes envían mensajes a /app/chat
    @SendTo("/topic/messages") // Los mensajes se envían a los suscriptores de /topic/messages
    public ChatMessage send(ChatMessage message) {
        // Aquí puedes guardar el mensaje en la base de datos si es necesario
        message.setTimestamp(System.currentTimeMillis());
        return message; // El mensaje se retransmite a todos los suscriptores
    }

    @PostMapping("/send")
    public ResponseEntity<ChatMessage> sendMessage(@RequestBody ChatMessage message) {
        // Aquí puedes guardar el mensaje en la base de datos si es necesario
        message.setTimestamp(System.currentTimeMillis());
        return ResponseEntity.ok(message);
    }
}
