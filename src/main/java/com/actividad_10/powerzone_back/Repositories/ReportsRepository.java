package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.Entities.Reports;
import com.actividad_10.powerzone_back.Entities.User;
import com.actividad_10.powerzone_back.Entities.emun.ReportState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    List<Reports> getReports(@Param("offset")int offset);

    @Modifying
    @Query("""
            update Reports R
            set R.type = :state
            where R.id = :id
            """)
    Integer updateState(int id, ReportState state);

    /**
     * Devuelve los usuarios sancionados
     * @param offset a partir de donde se tomaran los usuarios
     * @return los siguientes 50 usuarios sancionados
     */
    @Query("""
        select R
        from Reports R
        where R.type = :state
        order by R.type
        limit 50
        offset :offset
        """)
    List<User> userWarning(int offset, ReportState state);
}