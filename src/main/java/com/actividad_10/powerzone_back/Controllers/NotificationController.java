package com.actividad_10.powerzone_back.Controllers;

import com.actividad_10.powerzone_back.Config.JwtService;
import com.actividad_10.powerzone_back.DTOs.Notificaciones.BaseNotification;
import com.actividad_10.powerzone_back.Entities.Notification;
import com.actividad_10.powerzone_back.Services.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@AllArgsConstructor
public class NotificationController {
    NotificationService notificationService;
    JwtService jwtService;

    @GetMapping
    public List<BaseNotification> getNotification(@RequestHeader("Authorization") String token, @RequestParam(value = "offset", defaultValue = "0") long offaset){


        return notificationService.getNotification(token, offaset);
    }
}
