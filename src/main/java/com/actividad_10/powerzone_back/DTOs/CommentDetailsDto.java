package com.actividad_10.powerzone_back.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDetailsDto {
    private String content;
    private String nickname;
    private String avatar;
}