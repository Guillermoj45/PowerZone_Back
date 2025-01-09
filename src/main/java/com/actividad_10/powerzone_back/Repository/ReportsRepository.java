package com.actividad_10.powerzone_back.Repository;

import com.actividad_10.powerzone_back.Entities.Reports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReportsRepository extends JpaRepository<Reports, Long>, JpaSpecificationExecutor<Reports> {

}