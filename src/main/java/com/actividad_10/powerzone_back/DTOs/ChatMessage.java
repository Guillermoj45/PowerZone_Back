package com.actividad_10.powerzone_back.DTOs;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class ChatMessage {
    private String sender;
    private String content;
    private String timestamp;
    private Long groupId;
    private Long userId;

}
