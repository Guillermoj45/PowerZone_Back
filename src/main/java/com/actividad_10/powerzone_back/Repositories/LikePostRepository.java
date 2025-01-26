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
    @Query("DELETE FROM LikePost l WHERE l.userId = :userId AND l.postId = :postId")
    void deleteById(Long userId, Long postId);
}