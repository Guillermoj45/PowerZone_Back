package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.Entities.Comment;
import com.actividad_10.powerzone_back.Repositories.CommentRepository;

public class CommentService implements ICommentService {

    private CommentRepository commentRepository;
    @Override
    public void createComment(Comment newComment) {
        Comment comment = new Comment();
        comment.setUserId(newComment.getUserId());
        comment.setPostId(newComment.getPostId());
        comment.setContent(newComment.getContent());
        comment.setCreatedAt(java.time.LocalDate.now());
        commentRepository.save(comment);

    }

    @Override
    public void deleteComment(Long idComment) {
        commentRepository.deleteById(idComment);


    }

    @Override
    public void getCommentByUserName(Comment userComments) {
        Long userId = userComments.getUserId();
        commentRepository.findByUser_Id(userId);

    }
}
