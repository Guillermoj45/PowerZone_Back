package com.actividad_10.powerzone_back.DTOs;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileDto {
    private Long id;
    private String name;
    private String avatar;
    private LocalDate bornDate;
    private Boolean activo;
    private String nickname;
    private int followers;
    private int following;
}
