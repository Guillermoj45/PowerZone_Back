package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.Entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
    void deleteById(Long idPost);
    @Query("SELECT p.createdAt FROM Post p WHERE p.id = :id")
    Optional<LocalDateTime> findCreatedAtById(Long id);
    Optional<Post> findById(Long id);

    @Query("SELECT p FROM Post p JOIN LikePost l ON p.id = l.postId GROUP BY p.id ORDER BY COUNT(l.userId) DESC")
    List<Post> findPostsWithMostLikes();

    @Query("SELECT p FROM Post p WHERE p.user.id = :userId")
    List<Post> findAllByUserId(Long userId);
}