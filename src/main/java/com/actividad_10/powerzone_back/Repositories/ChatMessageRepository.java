package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.DTOs.GrupoUltimoMensajeDTO;
import com.actividad_10.powerzone_back.Entities.GroupMessenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<GroupMessenger, Long> {
    @Query("SELECT NEW com.actividad_10.powerzone_back.DTOs.GrupoUltimoMensajeDTO( " +
            "g.id, g.name, m.createdAt, m.message) " +
            "FROM GroupMessenger m " +
            "JOIN m.grupouser gu " +
            "JOIN gu.group g " +
            "WHERE m.createdAt = (SELECT MAX(m2.createdAt) FROM GroupMessenger m2 " +
            "                    JOIN m2.grupouser gu2 " +
            "                    WHERE gu2.group.id = g.id) " +
            "GROUP BY g.id, g.name, m.createdAt, m.message")
    List<GrupoUltimoMensajeDTO> obtenerUltimosMensajesPorGrupo();



}
