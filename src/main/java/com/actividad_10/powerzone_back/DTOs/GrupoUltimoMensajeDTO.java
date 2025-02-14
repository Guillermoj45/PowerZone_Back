package com.actividad_10.powerzone_back.DTOs;

import java.time.LocalDateTime;

public class GrupoUltimoMensajeDTO {
    private Long grupoId;
    private String grupoNombre;
    private LocalDateTime ultimoMensajeTimestamp; // Cambia Long a LocalDateTime

    public GrupoUltimoMensajeDTO(Long grupoId, String grupoNombre, LocalDateTime ultimoMensajeTimestamp) {
        this.grupoId = grupoId;
        this.grupoNombre = grupoNombre;
        this.ultimoMensajeTimestamp = ultimoMensajeTimestamp;
    }

    // Getters y Setters
    public Long getGrupoId() { return grupoId; }
    public String getGrupoNombre() { return grupoNombre; }
    public LocalDateTime getUltimoMensajeTimestamp() { return ultimoMensajeTimestamp; }
}
