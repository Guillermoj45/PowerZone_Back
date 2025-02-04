package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.DTOs.CommentDetailsDto;
import com.actividad_10.powerzone_back.Entities.Comment;
import com.actividad_10.powerzone_back.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT new com.actividad_10.powerzone_back.DTOs.CommentDetailsDto(c.content, u.profile.nickname, u.profile.avatar) " +
            "FROM Comment c JOIN c.user u WHERE c.post.id = :postId ORDER BY c.createdAt ASC")
    List<CommentDetailsDto> findAllCommentDetailsByPostId(Long postId);
    List<Comment> findByUser(User user);

}