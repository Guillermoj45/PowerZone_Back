package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.DTOs.ReportCountDto;
import com.actividad_10.powerzone_back.Entities.Report;
import com.actividad_10.powerzone_back.Entities.emun.ReportState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportsRepository extends JpaRepository<Report, Long> {

    @Query("""
            select R
            from Report R
            order by R.type, R.createdAt desc
            limit 50
            offset :offset
            """)
    List<Report> getReports(@Param("offset")int offset);

    @Modifying
    @Query("""
            update Report R
            set R.type = :state
            where R.id = :id
            """)
    void updateState(Long id, int state);

    /**
     * Devuelve los usuarios sancionados
     * @param offset a partir de donde se tomaran los usuarios
     * @return los siguientes 50 usuarios sancionados
     */
    @Query(value = """
            select PR.nickname, PR.avatar, count(R.*) as reports_count
            from reports R
                join post P on R.post_id = P.id and R.created_at_post = P.created_at
                join users U on P.user_id = U.id
                join profile PR on U.id = PR.id
            where R.type = 1
            group by PR.nickname, PR.avatar
            having 6 > count(R.*)
            limit 50
            offset :offset;
            """, nativeQuery = true)
    List<ReportCountDto> userWarning(int offset, ReportState state);



    @Query(value = """
            select PR.nickname, PR.avatar, count(R.*) as reports_count
            from reports R
                join post P on R.post_id = P.id and R.created_at_post = P.created_at
                join users U on P.user_id = U.id
                join profile PR on U.id = PR.id
            where R.type = 1
            group by PR.nickname, PR.avatar
            having count(R.*) > 5
            limit 50
            offset :offset;
            """, nativeQuery = true)
    List<ReportCountDto> userBanner(int offset, ReportState state);
}