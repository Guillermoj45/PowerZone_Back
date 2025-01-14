package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.DTOs.CreacionPerfilDTO;
import com.actividad_10.powerzone_back.DTOs.ProfileDto;
import com.actividad_10.powerzone_back.Entities.Profile;

public interface IUserService {
    void createUser(CreacionPerfilDTO nuevoPerfil);

    ProfileDto LoginUser(String email, String password);
}
