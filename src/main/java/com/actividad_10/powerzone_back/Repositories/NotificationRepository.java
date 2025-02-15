package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.Entities.Notification;
import com.actividad_10.powerzone_back.Entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("""
       select n
         from Notification n
         where n.userRecibe.id = :id
         order by n.createdAt desc
         limit 50
         offset :offset
       """)
    List<Notification> findNotificationByIdUser(Long id, Long offset);


    @Query("""
       select gu.user.profile
         from GroupName gn
            join GroupUser gu on gn.id = gu.group.id
         where gn.id = :idGroup
       """)
    List<Profile> getUserGroupUser(Long idGroup);


}