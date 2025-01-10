package com.actividad_10.powerzone_back.Repository;

import com.actividad_10.powerzone_back.Entities.LikePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LikePostRepository extends JpaRepository<LikePost, Long>{

}