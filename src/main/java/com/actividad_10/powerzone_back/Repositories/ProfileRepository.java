package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.Entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @Query("""
        SELECT p
        FROM Profile p
        WHERE LOWER(p.nickname) LIKE LOWER(CONCAT('%', :query, '%'))
        order by similarity(p.nickname, :query) desc
        """)
    List<Profile> findByNicknameContainingIgnoreCase(String query);
    @Query(value = "SELECT COUNT(*) FROM follower WHERE profile_id = :profileId", nativeQuery = true)
    int countFollowingByProfileId(@Param("profileId") Long profileId);

    @Query(value = "SELECT COUNT(*) FROM follower WHERE follower_id = :profileId", nativeQuery = true)
    int countFollowersByProfileId(@Param("profileId") Long profileId);

    @Query(value = """
        select *
        from (
            select p2.*
              from profile p
                       join follower f on p.id = f.profile_id
                       join profile p2 on f.follower_id = p2.id
              where p.id in (select p2.id
                             from follower f
                                      join profile p2 on f.follower_id = p2.id
                             where f.profile_id = :profileId)
                and p2.id not in (select p2.id
                                  from follower f
                                           join profile p2 on f.follower_id = p2.id
                                  where f.profile_id = :profileId)
                and p2.id != :profileId
              group by p2.id
              order by count(p2.nickname)
              )
            as p2 union distinct
                (
                    select p.*
                    from follower
                    join profile p on follower.follower_id = p.id
                        where p.id not in (
                            select f.follower_id
                            from follower f
                                join profile p on f.follower_id = p.id
                            where profile_id = :profileId
                            ) and p.id != :profileId
                    group by follower_id, p.id
                    order by count(follower_id) desc
                )
                limit 5;
        """, nativeQuery = true)
    List<Profile> getRecommendedProfiles(@Param("profileId") Long profileId);

    @Query(value = """
            select p.*
            from follower f
            join profile p on p.id = f.follower_id
            where f.profile_id = :id;
            """, nativeQuery = true)
    Set<Profile> findProfileWithFollowing(@Param("id") Long id);


    @Query("SELECT CASE WHEN p.banAt IS NULL OR p.banAt < CURRENT_DATE THEN FALSE ELSE TRUE END FROM Profile p WHERE p.id = :profileId")
    boolean isUserBanned(@Param("profileId") Long profileId);

}
