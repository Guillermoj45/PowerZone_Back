package com.actividad_10.powerzone_back.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RespuestaDto {
    private Integer estado;
    private String token;
    private String mensaje;
    private Object cuerpo;
}