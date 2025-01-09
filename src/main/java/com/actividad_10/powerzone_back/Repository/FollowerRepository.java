package com.actividad_10.powerzone_back.Repository;

import com.actividad_10.powerzone_back.Entities.Follower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FollowerRepository extends JpaRepository<Follower, Long>, JpaSpecificationExecutor<Follower> {

}