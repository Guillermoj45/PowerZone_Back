package com.actividad_10.powerzone_back.DTOs;

import com.actividad_10.powerzone_back.Entities.Rol;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreacionPerfilDTO {
    private String name;
    private String email;
    private String password;
    private Rol role;
    private String avatar;
    private LocalDate bornDate;
    private Boolean activo;

}


