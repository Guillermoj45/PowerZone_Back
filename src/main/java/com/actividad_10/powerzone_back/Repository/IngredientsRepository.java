package com.actividad_10.powerzone_back.Repository;

import com.actividad_10.powerzone_back.Entities.Ingredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IngredientsRepository extends JpaRepository<Ingredients, Long>, JpaSpecificationExecutor<Ingredients> {

}