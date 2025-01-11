package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.Entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;

public interface PostRepository extends JpaRepository<Post, Date>{

}