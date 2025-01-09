package com.actividad_10.powerzone_back.Repository;

import com.actividad_10.powerzone_back.Entities.Booksmarks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BooksmarksRepository extends JpaRepository<Booksmarks, Long>, JpaSpecificationExecutor<Booksmarks> {

}