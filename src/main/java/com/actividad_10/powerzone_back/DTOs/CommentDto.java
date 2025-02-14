package com.actividad_10.powerzone_back.DTOs;

import com.actividad_10.powerzone_back.Entities.Comment;
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

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.createdAt = comment.getCreatedAt();
        this.content = comment.getContent();
        this.userId = comment.getUser().getId();
        this.postId = comment.getPost().getId();
    }


}
