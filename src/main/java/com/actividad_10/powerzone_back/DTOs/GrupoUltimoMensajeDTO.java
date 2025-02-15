package com.actividad_10.powerzone_back.DTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GrupoUltimoMensajeDTO {
    private Long grupoId;
    private String grupoNombre;
    private LocalDateTime ultimoMensajeTimestamp;
    private String ultimoMensaje;

    public GrupoUltimoMensajeDTO(Long grupoId, String grupoNombre, LocalDateTime ultimoMensajeTimestamp, String ultimoMensaje) {
        this.grupoId = grupoId;
        this.grupoNombre = grupoNombre;
        this.ultimoMensajeTimestamp = ultimoMensajeTimestamp;
        this.ultimoMensaje = ultimoMensaje;
    }
}
