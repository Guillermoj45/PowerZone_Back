package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.Entities.GroupMessenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMessengerRepository extends JpaRepository<GroupMessenger, Long> {


    @Query("""
        select GM
        from GroupMessenger GM
        where GM.grupouser.group.id = :groupId
        """)
    List<GroupMessenger> findByGrupouser_GroupId(Long groupId);

    List<GroupMessenger> findByGrupouser_GroupIdOrderByCreatedAtAsc(Long groupId);

}