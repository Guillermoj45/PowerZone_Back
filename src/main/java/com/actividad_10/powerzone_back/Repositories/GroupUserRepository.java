package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.Entities.GroupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupUserRepository extends JpaRepository<GroupUser, Long>{
    GroupUser findByUserIdAndGroupId(Long userId, Long groupId);
    boolean existsByUserIdAndGroupId(Long userId, Long groupId);

    @Query("SELECT gu.group.id FROM GroupUser gu WHERE gu.user.id = :userId")
    List<Long> findGroupIdsByUserId(@Param("userId") Long userId);
}