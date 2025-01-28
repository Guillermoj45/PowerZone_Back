package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.Entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {

    Optional<Image> findByImage(String image);

}