package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.DTOs.CommentDetailsDto;
import com.actividad_10.powerzone_back.Entities.Post;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
    List<Post> findByUserId(Long userId);
    void deleteById(Long idPost);
    @Query("SELECT p.createdAt FROM Post p WHERE p.id = :id")
    Optional<LocalDateTime> findCreatedAtById(Long id);
    Optional<Post> findById(Long id);

    @Query("SELECT p FROM Post p JOIN LikePost l ON p.id = l.post.id GROUP BY p.id ORDER BY COUNT(l.user.id) DESC")
    List<Post> findPostsWithMostLikes();

    @Query("""
        select p
        from Post p
        where p.delete = false
        order by p.createdAt desc
        """)
    List<Post> findAllPosts();

    @Query("SELECT p FROM Post p WHERE p.user.id = :userId")
    List<Post> findAllByUserId(Long userId);
    @Query("SELECT COUNT(l) FROM LikePost l WHERE l.post.id = :postId")
    Long countLikesByPostId(Long postId);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
    Long countCommentsByPostId(Long postId);

    @Query("SELECT new com.actividad_10.powerzone_back.DTOs.CommentDetailsDto(c.content, u.profile.nickname, u.profile.avatar) " +
       "FROM Comment c JOIN c.user u WHERE c.post.id = :postId ORDER BY c.createdAt ASC")
    List<CommentDetailsDto> findFirstCommentDetailsByPostId(Long postId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.user.id IN :userIds")
    List<Post> findAllByUserIdIn(@Param("userIds") List<Long> userIds);
}