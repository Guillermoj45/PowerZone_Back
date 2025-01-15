package com.actividad_10.powerzone_back.Controllers;

import com.actividad_10.powerzone_back.DTOs.CreacionPerfilDTO;
import com.actividad_10.powerzone_back.DTOs.LoginDto;
import com.actividad_10.powerzone_back.DTOs.RespuestaDTO;
import com.actividad_10.powerzone_back.Entities.User;
import com.actividad_10.powerzone_back.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class
UserController {

    private UserService userService;

    @PostMapping("/create")
    ResponseEntity<Void> createUser(@RequestBody CreacionPerfilDTO nuevoPerfil) {
        userService.createUser(nuevoPerfil);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/login")
    ResponseEntity<RespuestaDTO> loginUser(@RequestBody LoginDto loginDto) {
        return userService.LoginUser(loginDto);
    }
}
