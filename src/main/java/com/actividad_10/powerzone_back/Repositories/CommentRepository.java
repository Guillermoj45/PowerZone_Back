package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.Entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByUser_Id(Long userId);

}