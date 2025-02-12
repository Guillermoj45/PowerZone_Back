package com.actividad_10.powerzone_back.Controllers;

import com.actividad_10.powerzone_back.Config.JwtService;
import com.actividad_10.powerzone_back.DTOs.Profile2Dto;
import com.actividad_10.powerzone_back.DTOs.ProfileDto;
import com.actividad_10.powerzone_back.DTOs.TokenDto;
import com.actividad_10.powerzone_back.Entities.Profile;
import com.actividad_10.powerzone_back.Entities.User;
import com.actividad_10.powerzone_back.Repositories.ProfileRepository;
import com.actividad_10.powerzone_back.Repositories.UserRepository;
import com.actividad_10.powerzone_back.Services.ProfileService;
import com.actividad_10.powerzone_back.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/profile")
@AllArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final ProfileService profileService;
    private final ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/getData")
    ResponseEntity<Profile2Dto> getProfile(@RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(userService.returnProfile(token), HttpStatus.OK);
    }

    @PostMapping("/updateData")
    HttpStatus updateData(@RequestHeader("Authorization") String token, @RequestBody Profile2Dto profile2Dto) throws IOException {
        userService.updateProfile(token, profile2Dto);
        return HttpStatus.OK;
    }

    @GetMapping("/search")
    public List<ProfileDto> searchUsers(@RequestParam String query) {
        return profileService.searchUsers(query);
    }

    @GetMapping("/{id}")
    public ProfileDto getUserById(@PathVariable Long id) {
        return profileService.getUserById(id);
    }

    @PostMapping("/{userId}/follow/{followUserId}")
    public ResponseEntity<Map<String, String>> followUser(
            @RequestHeader("Authorization") String token,
            @PathVariable Long userId,
            @PathVariable Long followUserId) {
        boolean followed = userService.followUser(token, followUserId);
        Map<String, String> response = new HashMap<>();
        if (followed) {
            response.put("message", "Followed successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "Unable to follow");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{userId}/unfollow/{unfollowUserId}")
    public ResponseEntity<Map<String, String>> unfollowUser(
            @RequestHeader("Authorization") String token,
            @PathVariable Long userId,
            @PathVariable Long unfollowUserId) {
        boolean unfollowed = userService.unfollowUser(token, unfollowUserId);
        Map<String, String> response = new HashMap<>();
        if (unfollowed) {
            response.put("message", "Unfollowed successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "Unable to unfollow");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{userId}/isFollowing/{followUserId}")
    public ResponseEntity<Boolean> isFollowing(
            @RequestHeader("Authorization") String token,
            @PathVariable Long userId,
            @PathVariable Long followUserId) {
        boolean isFollowing = userService.isFollowing(token, followUserId);
        return new ResponseEntity<>(isFollowing, HttpStatus.OK);
    }

    @GetMapping("/recommended")
    public List<ProfileDto> getRecommendedProfiles(@RequestHeader("Authorization") String token) {
        return profileService.getRecommendedProfiles(userService.returnProfile(token).getId());
    }

    @GetMapping("/following")
    public ResponseEntity<?> getFollowingProfiles(@RequestHeader("Authorization") String token) {
        try {
            // Elimina el prefijo "Bearer " del token
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // Extrae los datos del token
            TokenDto tokenData = jwtService.extractTokenData(token);

            // Obtén el email del token
            String email = tokenData.getEmail();

            // Busca al usuario por el email en la base de datos
            Optional<User> usuarioOpt = userRepository.findByEmail(email);

            if (usuarioOpt.isPresent()) {
                User usuario = usuarioOpt.get();

                // Busca el perfil correspondiente al usuario
                Set<Profile> profile = profileRepository.findProfileWithFollowing(usuario.getId());
                // Obtén los perfiles seguidos por este perfil

                // Mapea las entidades Profile a ProfileDto
                List<ProfileDto> followingDtos = profile.stream().map(following -> {
                    ProfileDto dto = new ProfileDto();
                    dto.setId(following.getId());
                    dto.setName(following.getName());
                    dto.setAvatar(following.getAvatar());
                    dto.setBornDate(following.getBornDate());
                    dto.setActivo(following.getActivo());
                    dto.setNickname(following.getNickname());
                    dto.setFollowers(following.getFollowers() != null ? following.getFollowers().size() : 0);
                    dto.setFollowing(following.getFollowing() != null ? following.getFollowing().size() : 0);
                    return dto;
                }).toList();

                // Devuelve la lista de perfiles seguidos
                return ResponseEntity.ok(Map.of(
                        "cantidad", followingDtos.size(),
                        "perfiles", followingDtos
                ));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no autorizado.");
        }
    }


}
