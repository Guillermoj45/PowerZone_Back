package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.Entities.Groupuser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupuserRepository extends JpaRepository<Groupuser, Long>{
    Groupuser findByUserIdAndGroupId(Long userId, Long groupId);
    boolean existsByUserIdAndGroupId(Long userId, Long groupId);

    @Query("SELECT gu.groupId FROM Groupuser gu WHERE gu.userId = :userId")
    List<Long> findGroupIdsByUserId(@Param("userId") Long userId);
}