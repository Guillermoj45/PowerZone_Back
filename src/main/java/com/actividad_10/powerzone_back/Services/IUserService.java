package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.DTOs.CreacionPerfilDto;
import com.actividad_10.powerzone_back.DTOs.LoginDto;
import com.actividad_10.powerzone_back.DTOs.Profile2Dto;
import com.actividad_10.powerzone_back.DTOs.RespuestaDto;
import org.springframework.http.ResponseEntity;

public interface IUserService {
    void createUser(CreacionPerfilDto nuevoPerfil);

    ResponseEntity<RespuestaDto> LoginUser(LoginDto loginDto);

    Profile2Dto returnProfile(String token);
}
