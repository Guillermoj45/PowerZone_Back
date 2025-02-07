package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.Entities.Groupname;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupnameRepository extends JpaRepository<Groupname, Long> {
    boolean existsByName(String name);
}