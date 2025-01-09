package com.actividad_10.powerzone_back.Repository;

import com.actividad_10.powerzone_back.Entities.Groupname;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GroupnameRepository extends JpaRepository<Groupname, Long>, JpaSpecificationExecutor<Groupname> {

}