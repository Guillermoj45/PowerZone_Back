package com.actividad_10.powerzone_back.DTOs;

import com.actividad_10.powerzone_back.Entities.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Post2Dto post;
    private String image_post;
    private String avatar;
    private String nickname;
    private Long numlikes = 0L;
    private Long numcomments = 0L;
    private String avatarcomment = " ";
    private String nicknamecomment = " ";
    private String firstcomment = "Se el primero en comentar esta publicación";
    private LocalDateTime createdAt;
}