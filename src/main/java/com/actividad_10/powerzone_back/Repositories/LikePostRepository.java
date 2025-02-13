package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.Entities.LikePost;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LikePostRepository extends JpaRepository<LikePost, Long>{
    @Modifying
    @Transactional
    @Query("DELETE FROM LikePost l WHERE l.user.id = :userId AND l.post.id = :postId")
    void deleteById(Long userId, Long postId);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM LikePost l WHERE l.user.id = :userId AND l.post.id = :postId")
    boolean existsByUserIdAndPostId(Long userId, Long postId);


}