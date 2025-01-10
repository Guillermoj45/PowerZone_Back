package com.actividad_10.powerzone_back.Controllers;

import com.actividad_10.powerzone_back.DTOs.CreacionPerfilDTO;
import com.actividad_10.powerzone_back.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    ResponseEntity<Void> createUser(CreacionPerfilDTO nuevoPerfil) {
        userService.createUser(nuevoPerfil);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
