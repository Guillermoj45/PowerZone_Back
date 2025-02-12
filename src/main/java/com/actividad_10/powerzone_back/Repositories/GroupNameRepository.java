package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.Entities.GroupName;
import org.hibernate.annotations.processing.SQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupNameRepository extends JpaRepository<GroupName, Long> {
    boolean existsByName(String name);

    @Query("""
        select GN
        from GroupName GN
                join GroupUser GU on GN.id = GU.group.id
                join GroupMessenger GM on GU = GM.grupouser
                where GU.user.id = :userId
        """)
    List<GroupName> getGroupsByUserId(Long userId);

    List<GroupName> getGroupNameById(Long id);
}