package com.actividad_10.powerzone_back.DTOs;

import com.actividad_10.powerzone_back.Entities.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Post post;
    private String avatar;
    private String nickname;
}