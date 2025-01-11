package com.actividad_10.powerzone_back.DTOs;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreacionPerfilDTO {
    private String name;
    private String email;
    private String password;
    private Integer role;
    private String avatar;
    private LocalDate bornDate;
    private Boolean activo;

}


