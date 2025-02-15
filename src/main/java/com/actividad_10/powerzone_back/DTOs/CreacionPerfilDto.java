package com.actividad_10.powerzone_back.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreacionPerfilDto {
    private String name;
    private String email;
    private String nickname;
    private String password;
    private String avatar;
    private LocalDate bornDate;
    private Boolean activo;
}
