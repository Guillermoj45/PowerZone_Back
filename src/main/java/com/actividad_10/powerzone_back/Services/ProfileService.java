package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.Config.JwtService;
import com.actividad_10.powerzone_back.DTOs.ProfileDto;
import com.actividad_10.powerzone_back.DTOs.TokenDto;
import com.actividad_10.powerzone_back.Entities.Profile;
import com.actividad_10.powerzone_back.Entities.User;
import com.actividad_10.powerzone_back.Repositories.ProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private JwtService jwtService;

    public List<ProfileDto> searchUsers(String query) {

        if (query == null || query.trim().isEmpty()) {
            return List.of(); // Devuelve una lista vacía si no hay consulta
        }

        List<Profile> profiles = profileRepository.findByNicknameContainingIgnoreCase(query);

        // Convertir las entidades a DTOs
        return profiles.stream().map(this::trasformateProfileToDto).collect(Collectors.toList());
    }

    public ProfileDto getUserById(Long id) {
        // Buscar el perfil por ID en el repositorio
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + id));

        // Convertir la entidad Profile a ProfileDto
        return trasformateProfileToDto(profile);
    }

    public Profile getProfileById(Long id) {
        // Buscar el perfil por ID en el repositorio

        // Convertir la entidad Profile a ProfileDto
        return profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + id));
    }


    public Profile save(Profile profile) {
        return profileRepository.save(profile);
    }

    public List<ProfileDto> getRecommendedProfiles(Long profileId) {
        List<ProfileDto> recommendedProfiles = new ArrayList<>();
        profileRepository.getRecommendedProfiles(profileId).forEach(profile -> {
            ProfileDto dto = trasformateProfileToDto(profile);
            recommendedProfiles.add(dto);
        });
        return recommendedProfiles;
    }

    private ProfileDto trasformateProfileToDto(Profile profile) {
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
