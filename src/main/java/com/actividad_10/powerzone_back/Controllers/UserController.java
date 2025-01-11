package com.actividad_10.powerzone_back.Controllers;

import com.actividad_10.powerzone_back.DTOs.CreacionPerfilDTO;
import com.actividad_10.powerzone_back.Entities.Profile;
import com.actividad_10.powerzone_back.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @PostMapping("/create")
    ResponseEntity<Void> createUser(@RequestBody CreacionPerfilDTO nuevoPerfil) {
        userService.createUser(nuevoPerfil);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/login")
    ResponseEntity<Profile> loginUser(@RequestParam String email, @RequestParam String password) {
        return new ResponseEntity<>(userService.LoginUser(email, password), HttpStatus.OK);
    }
}
