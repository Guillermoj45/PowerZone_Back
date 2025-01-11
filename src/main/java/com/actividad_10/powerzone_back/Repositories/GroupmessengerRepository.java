package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.Entities.GroupMessenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;

public interface GroupmessengerRepository extends JpaRepository<GroupMessenger, Date> {

}