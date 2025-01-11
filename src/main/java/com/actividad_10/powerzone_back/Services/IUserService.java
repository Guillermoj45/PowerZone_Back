package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.DTOs.CreacionPerfilDTO;
import com.actividad_10.powerzone_back.Entities.Profile;

public interface IUserService {
    void createUser(CreacionPerfilDTO nuevoPerfil);

    Profile LoginUser(String email, String password);
}
