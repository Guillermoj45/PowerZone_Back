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
            select PR.nickname, PR.avatar, count(distinct P.id) as reports_count
            from reports R
                join post P on R.post_id = P.id and R.created_at_post = P.created_at
                join users U on P.user_id = U.id
                join profile PR on U.id = PR.id
            where R.type = 1
            group by PR.nickname, PR.avatar
            having 6 > count(distinct P.id)
            limit 50
            offset :offset;
            """, nativeQuery = true)
    List<ReportCountDto> userWarning(int offset, ReportState state);



    @Query(value = """
            select PR.nickname, PR.avatar, count(distinct P.id) as reports_count
            from reports R
                join post P on R.post_id = P.id and R.created_at_post = P.created_at
                join users U on P.user_id = U.id
                join profile PR on U.id = PR.id
            where R.type = 1
            group by PR.nickname, PR.avatar
            having count(distinct P.id) > 5
            limit 50
            offset :offset;
            """, nativeQuery = true)
    List<ReportCountDto> userBanner(int offset, ReportState state);

    public List<Report> findByPost_Id(Long id);

    @Query(value = """
            select count(distinct p.id) as num_reports
             from reports r
                 join post p on r.created_at_post = p.created_at and r.post_id = p.id
             where p.user_id = :id_user and r.type = 1;
            """, nativeQuery = true)
    Integer countReports(@Param("id_user") Long id);
}