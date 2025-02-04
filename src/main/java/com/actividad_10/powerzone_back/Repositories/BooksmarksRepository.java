package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.Entities.Booksmarks;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksmarksRepository extends JpaRepository<Booksmarks, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Booksmarks b WHERE b.userId = :userId AND b.postId = :postId")
    void deleteById(Long userId, Long postId);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Booksmarks b WHERE b.userId = :userId AND b.postId = :postId")
    boolean existsByUserIdAndPostId(Long userId, Long postId);
}