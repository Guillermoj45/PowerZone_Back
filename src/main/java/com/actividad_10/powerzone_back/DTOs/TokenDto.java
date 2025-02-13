package com.actividad_10.powerzone_back.DTOs;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class TokenDto {
    private String email;
    private String rol;
    private Long fecha_creacion;
    private Long fecha_expiracion;
}
