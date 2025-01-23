package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.Entities.Reports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportsRepository extends JpaRepository<Reports, Long> {

}