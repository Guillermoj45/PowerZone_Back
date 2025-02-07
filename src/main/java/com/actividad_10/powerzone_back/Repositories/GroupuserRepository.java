package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.Entities.Groupuser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupuserRepository extends JpaRepository<Groupuser, Long>{
    Groupuser findByUserIdAndGroupId(Long userId, Long groupId);
    boolean existsByUserIdAndGroupId(Long userId, Long groupId);

}