package com.actividad_10.powerzone_back.DTOs;

import lombok.Data;

@Data
public class ChatMessage {
    private String sender;
    private String content;
    private Long timestamp;
    private Long groupId;
    private Long userId;

}
