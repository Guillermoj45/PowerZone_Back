package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.Entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Optional;
@Repository
public interface PostRepository extends JpaRepository<Post, Date>{
    void deleteById(Long idPost);
    Optional<LocalDateTime> findCreatedAtById(Long id);
    Optional<Post> findById(Long id);
}