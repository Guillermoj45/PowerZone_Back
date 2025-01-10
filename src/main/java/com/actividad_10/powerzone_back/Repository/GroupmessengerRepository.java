package com.actividad_10.powerzone_back.Repository;

import com.actividad_10.powerzone_back.Entities.Groupmessenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.sql.Date;

public interface GroupmessengerRepository extends JpaRepository<Groupmessenger, Date> {

}