package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.DTOs.CreacionPerfilDTO;
import com.actividad_10.powerzone_back.Entities.Profile;
import com.actividad_10.powerzone_back.Entities.User;
import com.actividad_10.powerzone_back.Repository.ProfileRepository;
import com.actividad_10.powerzone_back.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public void createUser(CreacionPerfilDTO nuevoPerfil) {
        // Crear el usuario
        User user = new User();
        user.setEmail(nuevoPerfil.getEmail());
        user.setPassword(nuevoPerfil.getPassword());
        user.setRole(nuevoPerfil.getRole());
        User createdUser = userRepository.save(user);

        // Crear el perfil
        Profile profile = new Profile();
        profile.setId(createdUser.getId());
        profile.setName(nuevoPerfil.getName());
        profile.setAvatar(nuevoPerfil.getAvatar());
        profile.setBornDate(nuevoPerfil.getBornDate());
        profile.setCreatedAt(LocalDate.now());
        profile.setActivo(true);
        profileRepository.save(profile);
    }
}
