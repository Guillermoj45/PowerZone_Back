package com.actividad_10.powerzone_back.Repository;

import com.actividad_10.powerzone_back.Entities.Exercises;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ExercisesRepository extends JpaRepository<Exercises, Long>, JpaSpecificationExecutor<Exercises> {

}