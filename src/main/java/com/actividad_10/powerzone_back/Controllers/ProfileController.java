package com.actividad_10.powerzone_back.Controllers;

import com.actividad_10.powerzone_back.DTOs.Profile2Dto;
import com.actividad_10.powerzone_back.DTOs.ProfileDto;
import com.actividad_10.powerzone_back.Services.ProfileService;
import com.actividad_10.powerzone_back.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    private final ProfileService profileService;

    public ProfileController(UserService userService, ProfileService profileService) {
        this.userService = userService;
        this.profileService = profileService;
    }

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
    public ResponseEntity<String> followUser(
            @RequestHeader("Authorization") String token,
            @PathVariable Long userId,
            @PathVariable Long followUserId) {
        boolean followed = userService.followUser(userId, followUserId);
        if (followed) {
            return new ResponseEntity<>("Followed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unable to follow", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{userId}/unfollow/{unfollowUserId}")
    public ResponseEntity<String> unfollowUser(
            @RequestHeader("Authorization") String token,
            @PathVariable Long userId,
            @PathVariable Long unfollowUserId) {
        boolean unfollowed = userService.unfollowUser(userId, unfollowUserId);
        if (unfollowed) {
            return new ResponseEntity<>("Unfollowed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unable to unfollow", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{userId}/isFollowing/{followUserId}")
    public ResponseEntity<Boolean> isFollowing(
            @RequestHeader("Authorization") String token,
            @PathVariable Long userId,
            @PathVariable Long followUserId) {
        boolean isFollowing = userService.isFollowing(userId, followUserId);
        return new ResponseEntity<>(isFollowing, HttpStatus.OK);
    }
}
