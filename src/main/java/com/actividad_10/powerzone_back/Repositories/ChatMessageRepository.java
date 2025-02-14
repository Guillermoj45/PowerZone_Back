package com.actividad_10.powerzone_back.Repositories;

import com.actividad_10.powerzone_back.DTOs.GrupoUltimoMensajeDTO;
import com.actividad_10.powerzone_back.Entities.GroupMessenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<GroupMessenger, Long> {
    @Query("SELECT NEW com.actividad_10.powerzone_back.DTOs.GrupoUltimoMensajeDTO(g.id, g.name, MAX(m.createdAt)) " +
            "FROM GroupMessenger m " +
            "JOIN m.grupouser gu " +
            "JOIN gu.group g " +
            "GROUP BY g.id, g.name")
    List<GrupoUltimoMensajeDTO> obtenerUltimosMensajesPorGrupo();


}
