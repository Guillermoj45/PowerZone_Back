package com.actividad_10.powerzone_back.Controllers;

import com.actividad_10.powerzone_back.Config.JwtService;
import com.actividad_10.powerzone_back.DTOs.ReportsAdminDto;
import com.actividad_10.powerzone_back.Entities.emun.Rol;
import com.actividad_10.powerzone_back.Exceptions.NoAdmin;
import com.actividad_10.powerzone_back.Services.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private ReportService reportService;
    private JwtService jwtService;

    @GetMapping()
    public List<ReportsAdminDto> getReports(@RequestHeader("Authorization") String token, @RequestParam(value = "offser", defaultValue = "0") int offset){
        String jwt = token.replace("Bearer ", "");
        String rol = jwtService.extractTokenData(jwt).getRol();

        if (rol.equals(Rol.ADMIN.toString())){
            return reportService.getReports(offset);
        }
        throw new NoAdmin("Necesitas ser administrador para esto");
    }
}
