package com.actividad_10.powerzone_back.DTOs;

import com.actividad_10.powerzone_back.Entities.emun.Rol;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
