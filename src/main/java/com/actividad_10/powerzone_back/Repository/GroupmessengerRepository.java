package com.actividad_10.powerzone_back.Repository;

import com.actividad_10.powerzone_back.Entities.GroupMessenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.sql.Date;

public interface GroupmessengerRepository extends JpaRepository<GroupMessenger, Date> {

}