package com.actividad_10.powerzone_back.DTOs;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private ProfileDto profile;
}
