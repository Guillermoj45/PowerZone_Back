package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.Entities.Comment;

public interface ICommentService {
    void createComment(String token, Comment newComment);
     void deleteComment(Long idComment);

    void getCommentByUserName(Comment userComments);
}
