package com.actividad_10.powerzone_back.Controllers;

import com.actividad_10.powerzone_back.DTOs.Profile2Dto;
import com.actividad_10.powerzone_back.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @PostMapping("/getData")
    ResponseEntity<Profile2Dto> getProfile(@RequestHeader("Authorization") String token){
        return new ResponseEntity<>(userService.returnProfile(token), HttpStatus.OK);
    }
}
