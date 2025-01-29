package com.actividad_10.powerzone_back.DTOs;

import lombok.Data;

@Data
public class ChatMessage {
    private String sender;   // Nombre del usuario que envía el mensaje
    private String content;  // Contenido del mensaje
    private Long timestamp;  // Marca de tiempo del mensaje
    private String groupId;  // Id del nombre del grupo
}
