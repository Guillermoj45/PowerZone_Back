package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.Entities.Reports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportsRepository extends JpaRepository<Reports, Long> {

    @Query("""
            select R
            from Reports R
            order by R.type
            limit 50
            offset :offset
            """)
    public List<Reports> getReports(@Param("offset")int offset);
}