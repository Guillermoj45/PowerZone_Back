package com.actividad_10.powerzone_back.DTOs;

import com.actividad_10.powerzone_back.Entities.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Post2Dto post;
    private String avatar;
    private String nickname;
    private Long numlikes = 0L;
    private Long numcomments = 0L;
    private String avatarcomment = "https://picsum.photos/800/400?random=1";
    private String nicknamecomment = "Usuario1";
    private String firstcomment = "Se el primero en comentar esta publicación";

}