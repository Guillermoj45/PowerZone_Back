package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsUserByEmail(String email);

    @Query("SELECT u.password FROM User u WHERE u.email = :email")
    Optional<String> findPasswordByEmail(@Param("email") String email);


    @Modifying
    @Query(value = "INSERT INTO follower (profile_id, follower_id) VALUES (:userId, :followerId)", nativeQuery = true)
    void followUser(@Param("userId") Long userId, @Param("followerId") Long followerId);

    @Modifying
    @Query(value = "DELETE FROM follower WHERE profile_id = :userId AND follower_id = :followerId", nativeQuery = true)
    void unfollowUser(@Param("userId") Long userId, @Param("followerId") Long followerId);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM follower WHERE profile_id = :userId AND follower_id = :followerId)", nativeQuery = true)

    boolean isFollowing(@Param("userId") Long userId, @Param("followerId") Long followerId);
    @Query("SELECT f.id FROM Profile p JOIN p.followers f WHERE p.id = :userId")
    List<Long> findFollowedUserIdsByUserId(@Param("userId") Long userId);

}