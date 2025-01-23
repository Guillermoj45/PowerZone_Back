package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.Entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

}