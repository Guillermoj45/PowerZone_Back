package com.actividad_10.powerzone_back.DTOs;

import com.actividad_10.powerzone_back.Entities.emun.Rol;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreacionPerfilDto {
    private String name;
    private String email;
    private String nickname; // Ensure this field is present
    private String password;
    private String avatar;
    private LocalDate bornDate;
    private Boolean activo;
}
