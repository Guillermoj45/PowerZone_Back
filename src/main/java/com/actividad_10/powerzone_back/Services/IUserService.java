package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.DTOs.CreacionPerfilDTO;
import com.actividad_10.powerzone_back.DTOs.LoginDto;
import com.actividad_10.powerzone_back.DTOs.ProfileDto;
import com.actividad_10.powerzone_back.DTOs.RespuestaDTO;
import com.actividad_10.powerzone_back.Entities.User;
import org.springframework.http.ResponseEntity;

public interface IUserService {
    void createUser(CreacionPerfilDTO nuevoPerfil);

    ResponseEntity<RespuestaDTO> LoginUser(LoginDto loginDto);
}
