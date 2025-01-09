package com.actividad_10.powerzone_back.Repository;

import com.actividad_10.powerzone_back.Entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.sql.Date;

public interface PostRepository extends JpaRepository<Post, Date>, JpaSpecificationExecutor<Post> {

}