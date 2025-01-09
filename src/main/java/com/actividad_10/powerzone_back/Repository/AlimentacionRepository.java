package com.actividad_10.powerzone_back.Repository;

import com.actividad_10.powerzone_back.Entities.Alimentacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AlimentacionRepository extends JpaRepository<Alimentacion, Long>, JpaSpecificationExecutor<Alimentacion> {

}