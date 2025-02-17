package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.Entities.Diet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {

}