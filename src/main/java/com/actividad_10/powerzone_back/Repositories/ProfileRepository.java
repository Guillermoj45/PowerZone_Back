package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.Entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @Query("SELECT p FROM Profile p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Profile> findByNameContainingIgnoreCase(String query);
    @Query(value = "SELECT COUNT(*) FROM follower WHERE profile_id = :profileId", nativeQuery = true)
    int countFollowingByProfileId(@Param("profileId") Long profileId);

    @Query(value = "SELECT COUNT(*) FROM follower WHERE follower_id = :profileId", nativeQuery = true)
    int countFollowersByProfileId(@Param("profileId") Long profileId);

    @Query(value = """
        select p2.*
        from profile p
            join follower f on p.id = f.profile_id
            join profile p2 on f.follower_id = p2.id
            where p.id in (
                select p2.id
                from follower f
                join profile p2 on f.follower_id = p2.id
                where f.profile_id = :profileId
            ) and p2.id not in (
                select p2.id
                from follower f
                join profile p2 on f.follower_id = p2.id
                where f.profile_id = :profileId
            ) and p2.id != :profileId
        group by p2.id
        order by count(p2.nickname)
        limit 5;
    """, nativeQuery = true)
    List<Profile> getRecommendedProfiles(@Param("profileId") Long profileId);

    Optional<Profile> findByUserId(Long userId);

}
