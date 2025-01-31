package com.actividad_10.powerzone_back.DTOs;

import com.actividad_10.powerzone_back.Entities.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

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
