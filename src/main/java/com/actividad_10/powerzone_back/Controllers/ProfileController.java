package com.actividad_10.powerzone_back.Controllers;

import com.actividad_10.powerzone_back.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @PostMapping("/getData")
    HttpStatus getProfile(@RequestHeader("Authorization") String token){
        userService.returnProfile(token);
        return HttpStatus.OK;
    }
}
