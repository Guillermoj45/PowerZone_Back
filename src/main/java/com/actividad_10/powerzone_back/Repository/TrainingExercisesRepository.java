package com.actividad_10.powerzone_back.Repository;

import com.actividad_10.powerzone_back.Entities.TrainingExercises;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TrainingExercisesRepository extends JpaRepository<TrainingExercises, Long>, JpaSpecificationExecutor<TrainingExercises> {

}