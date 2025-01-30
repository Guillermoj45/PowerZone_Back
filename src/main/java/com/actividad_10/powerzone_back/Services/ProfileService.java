package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.DTOs.ProfileDto;
import com.actividad_10.powerzone_back.Entities.Profile;
import com.actividad_10.powerzone_back.Repositories.ProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    public List<ProfileDto> searchUsers(String query) {

        if (query == null || query.trim().isEmpty()) {
            return List.of(); // Devuelve una lista vacía si no hay consulta
        }

        List<Profile> profiles = profileRepository.findByNameContainingIgnoreCase(query);

        // Convertir las entidades a DTOs
        return profiles.stream().map(profile -> {
            ProfileDto dto = new ProfileDto();
            dto.setId(profile.getId());
            dto.setName(profile.getName());
            dto.setAvatar(profile.getAvatar());
            dto.setBornDate(profile.getBornDate());
            dto.setActivo(profile.getActivo());
            return dto;
        }).collect(Collectors.toList());
    }

    public ProfileDto getUserById(Long id) {
        // Buscar el perfil por ID en el repositorio
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + id));

        // Convertir la entidad Profile a ProfileDto
        ProfileDto dto = new ProfileDto();
        dto.setId(profile.getId());
        dto.setName(profile.getName());
        dto.setAvatar(profile.getAvatar());
        dto.setBornDate(profile.getBornDate());
        dto.setActivo(profile.getActivo());
        dto.setNickname(profile.getNickname());

        return dto;
    }

}
