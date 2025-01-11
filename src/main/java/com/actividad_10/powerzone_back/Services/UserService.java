package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.DTOs.CreacionPerfilDTO;
import com.actividad_10.powerzone_back.Entities.Profile;
import com.actividad_10.powerzone_back.Entities.User;
import com.actividad_10.powerzone_back.Exceptions.BlankInfo;
import com.actividad_10.powerzone_back.Exceptions.ExistingField;
import com.actividad_10.powerzone_back.Repository.ProfileRepository;
import com.actividad_10.powerzone_back.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void createUser(CreacionPerfilDTO nuevoPerfil) {
        // Validar datos del DTO
        if (nuevoPerfil.getEmail() == null || nuevoPerfil.getPassword() == null || nuevoPerfil.getName() == null) {
            throw new BlankInfo("Email, password y nombre son obligatorios.");
        }

        // Verificar si el email ya existe
        if (userRepository.findByEmail(nuevoPerfil.getEmail()).isPresent()) {
            throw new ExistingField("El email ya está en uso.");
        }

        // Crear usuario
        User user = new User();
        user.setEmail(nuevoPerfil.getEmail());
        user.setPassword(nuevoPerfil.getPassword());
        user.setRole(nuevoPerfil.getRole() != null ? nuevoPerfil.getRole() : 0);

        // Crear perfil asociado
        Profile profile = new Profile();
        profile.setName(nuevoPerfil.getName());
        profile.setAvatar(nuevoPerfil.getAvatar() != null ? nuevoPerfil.getAvatar() :
                "https://res.cloudinary.com/dflz0gveu/image/upload/v1718394870/avatars/default.png");
        profile.setBornDate(nuevoPerfil.getBornDate());
        profile.setCreatedAt(LocalDate.now());
        profile.setActivo(nuevoPerfil.getActivo() != null ? nuevoPerfil.getActivo() : true);

        // Asignar el perfil al usuario
        user.setProfile(profile);

        // Guardar usuario (el perfil se guarda automáticamente)
        userRepository.save(user);
    }



}
