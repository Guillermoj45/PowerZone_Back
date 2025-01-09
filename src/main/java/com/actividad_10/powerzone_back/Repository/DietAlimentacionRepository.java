package com.actividad_10.powerzone_back.Repository;

import com.actividad_10.powerzone_back.Entities.DietAlimentacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DietAlimentacionRepository extends JpaRepository<DietAlimentacion, Long>, JpaSpecificationExecutor<DietAlimentacion> {

}