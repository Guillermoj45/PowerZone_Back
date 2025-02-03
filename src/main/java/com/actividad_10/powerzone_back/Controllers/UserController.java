package com.actividad_10.powerzone_back.Controllers;

import com.actividad_10.powerzone_back.DTOs.CreacionPerfilDto;
import com.actividad_10.powerzone_back.DTOs.LoginDto;
import com.actividad_10.powerzone_back.DTOs.RespuestaDto;
import com.actividad_10.powerzone_back.Email.PasswordRecoveryController;
import com.actividad_10.powerzone_back.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private UserService userService;
    private PasswordRecoveryController passwordRecoveryController;

    @PostMapping("/create")
    public ResponseEntity<Void> createUser(@RequestBody CreacionPerfilDto nuevoPerfil) {
        userService.createUser(nuevoPerfil);
        String emailResponse = passwordRecoveryController.sendWelComeEmail(nuevoPerfil.getEmail());
        if (emailResponse.startsWith("Correo de bienvenida enviado")) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    ResponseEntity<RespuestaDto> loginUser(@RequestBody LoginDto loginDto) {
        return userService.LoginUser(loginDto);
    }
}
