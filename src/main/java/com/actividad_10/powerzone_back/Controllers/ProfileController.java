package com.actividad_10.powerzone_back.Controllers;

import com.actividad_10.powerzone_back.DTOs.Profile2Dto;
import com.actividad_10.powerzone_back.DTOs.ProfileDto;
import com.actividad_10.powerzone_back.Entities.Profile;
import com.actividad_10.powerzone_back.Repositories.ProfileRepository;
import com.actividad_10.powerzone_back.Services.ProfileService;
import com.actividad_10.powerzone_back.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/profile")
@AllArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final ProfileService profileService;
    private final ProfileRepository profileRepository;

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

    @GetMapping("/{profileId}/following")
    public ResponseEntity<Set<Profile>> getFollowingProfiles(@PathVariable Long profileId) {
        // Busca el perfil del usuario actual por su ID
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + profileId));

        System.out.println("Cantidad de perfiles seguidos: " + profile.getFollowing().size());

        return ResponseEntity.ok(profile.getFollowing());

    }
}
