package com.actividad_10.powerzone_back.Repository;

import com.actividad_10.powerzone_back.Entities.AlimentacionIngredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AlimentacionIngredientsRepository extends JpaRepository<AlimentacionIngredients, Long>, JpaSpecificationExecutor<AlimentacionIngredients> {

}