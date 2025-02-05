package com.actividad_10.powerzone_back.Controllers;

import com.actividad_10.powerzone_back.Config.JwtService;
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
    private JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<Void> createUser(@RequestBody CreacionPerfilDto nuevoPerfil) {
        userService.createUser(nuevoPerfil);
        passwordRecoveryController.sendWelComeEmail(nuevoPerfil.getEmail());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PostMapping("/login")
    ResponseEntity<RespuestaDto> loginUser(@RequestBody LoginDto loginDto) {
        return userService.LoginUser(loginDto);
    }


    @PostMapping("/isTutorialComplete")
    ResponseEntity<Boolean> IsmarkTutorialDone(@RequestHeader("Authorization") String token) {
        boolean isNewUser = userService.isTutorialDone(token);
        return new ResponseEntity<>(isNewUser, HttpStatus.OK);
    }

    @PostMapping("/tutorialComplete")
    ResponseEntity<?> markTutorialDone(@RequestHeader("Authorization") String token){
        userService.tutorialDone(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/ImAdmin")
    ResponseEntity<Boolean> ImAdmin(@RequestHeader(value = "Authorization", defaultValue = "Hola") String token) {
        String jwt = token.replace("Bearer ", "");
        String rol = jwtService.extractTokenData(jwt).getRol();
        if (rol.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        }

        return ResponseEntity.status(HttpStatus.OK).body(false);
    }
}
