package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.Entities.Comment;
import com.actividad_10.powerzone_back.Entities.User;
import com.actividad_10.powerzone_back.Repositories.CommentRepository;

import java.time.LocalDate;
import java.util.List;

public class CommentService implements ICommentService {

    private CommentRepository commentRepository;
    @Override
    public void createComment(Comment newComment) {
        newComment.setCreatedAt(LocalDate.now());
        commentRepository.save(newComment);
    }

    @Override
    public void deleteComment(Long idComment) {
        commentRepository.deleteById(idComment);


    }

    @Override
    public void getCommentByUserName(Comment userComments) {
        User userId = userComments.getUser();
        List<Comment> hola = commentRepository.findByUser(userId);

    }
}
