package com.actividad_10.powerzone_back.Repository;

import com.actividad_10.powerzone_back.Entities.Diet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DietRepository extends JpaRepository<Diet, Long> {

}