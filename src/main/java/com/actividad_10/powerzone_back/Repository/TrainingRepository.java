package com.actividad_10.powerzone_back.Repository;

import com.actividad_10.powerzone_back.Entities.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TrainingRepository extends JpaRepository<Training, Long>, JpaSpecificationExecutor<Training> {

}