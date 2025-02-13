package com.actividad_10.powerzone_back.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private LocalDateTime createdAt;
    private String content;
    private Long userId;
    private Long postId;


}
