package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.DTOs.CommentDto;
import com.actividad_10.powerzone_back.Entities.Comment;

public interface ICommentService {
    CommentDto createComment(String token, Comment newComment);
    void deleteComment(String token, Comment deleteComment);

    void getCommentByUserName(Comment userComments);
}
