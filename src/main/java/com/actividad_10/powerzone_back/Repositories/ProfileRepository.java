package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.Entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @Query("SELECT p FROM Profile p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Profile> findByNameContainingIgnoreCase(String query);
    @Query(value = "SELECT COUNT(*) FROM follower WHERE profile_id = :profileId", nativeQuery = true)
    int countFollowingByProfileId(@Param("profileId") Long profileId);

    @Query(value = "SELECT COUNT(*) FROM follower WHERE follower_id = :profileId", nativeQuery = true)
    int countFollowersByProfileId(@Param("profileId") Long profileId);
}
