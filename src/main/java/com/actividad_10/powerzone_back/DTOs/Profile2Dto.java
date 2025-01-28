package com.actividad_10.powerzone_back.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Profile2Dto {
    private Long id;
    private String name;
    private String email;
    private LocalDate bornDate;
    private String nickName;
    private String avatar;
}
