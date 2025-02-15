package com.actividad_10.powerzone_back.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post2Dto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String images;
    private Boolean delete;
    private Long userId;
}
