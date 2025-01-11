package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.Entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}