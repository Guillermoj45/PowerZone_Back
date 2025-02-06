package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.DTOs.UserIdDto;
import com.actividad_10.powerzone_back.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsUserByEmail(String email);

    @Query("SELECT u.password FROM User u WHERE u.email = :email")
    Optional<String> findPasswordByEmail(@Param("email") String email);

    @Query("SELECT new com.actividad_10.powerzone_back.DTOs.UserIdDto(u.id) FROM User u WHERE u.id = :id")
    Optional<UserIdDto> findByUserId(Long id);
    @Modifying
    @Query(value = "INSERT INTO follower (user_id, follower_id) VALUES (:userId, :followerId)", nativeQuery = true)
    void followUser(@Param("userId") Long userId, @Param("followerId") Long followerId);

    @Modifying
    @Query(value = "DELETE FROM follower WHERE user_id = :userId AND follower_id = :followerId", nativeQuery = true)
    void unfollowUser(@Param("userId") Long userId, @Param("followerId") Long followerId);
    @Query(value = "SELECT EXISTS(SELECT 1 FROM follower WHERE user_id = :userId AND follower_id = :followerId)", nativeQuery = true)
    boolean isFollowing(@Param("userId") Long userId, @Param("followerId") Long followerId);

}