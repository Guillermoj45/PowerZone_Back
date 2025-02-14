package com.actividad_10.powerzone_back.chatBot;

import com.actividad_10.powerzone_back.DTOs.CreacionPerfilDto;
import com.actividad_10.powerzone_back.DTOs.Profile2Dto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatBot")
public class ChatBotController {

    @Autowired
    private ChatBotService chatBotService;

    @PostMapping("/chat")
    public String createUser(@RequestHeader("Authorization") String token, @RequestBody String mensaje) {
        String response = chatBotService.holaBot(mensaje);
        return response;
    }
}
