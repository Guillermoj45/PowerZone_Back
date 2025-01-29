package com.actividad_10.powerzone_back.Controllers;

import com.actividad_10.powerzone_back.Config.JwtService;
import com.actividad_10.powerzone_back.DTOs.ChangeStateReportDto;
import com.actividad_10.powerzone_back.DTOs.ReportCountDto;
import com.actividad_10.powerzone_back.DTOs.ReportsAdminDto;
import com.actividad_10.powerzone_back.Entities.emun.Rol;
import com.actividad_10.powerzone_back.Exceptions.NoAdmin;
import com.actividad_10.powerzone_back.Services.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private ReportService reportService;
    private JwtService jwtService;

    @GetMapping()
    public List<ReportsAdminDto> getReports(@RequestHeader("Authorization") String token, @RequestParam(value = "offser", defaultValue = "0") int offset, Principal principal){
        String jwt = token.replace("Bearer ", "");
        String rol = jwtService.extractTokenData(jwt).getRol();

        if (Rol.ADMIN.name().equals(rol)){
            return reportService.getReports(offset);
        }
        throw new NoAdmin("Necesitas ser administrador para esto");
    }

    @PutMapping("/report")
    public void updateState(@RequestHeader("Authorization") String token, @RequestBody ChangeStateReportDto changeStateReportDto){
        String jwt = token.replace("Bearer ", "");
        String rol = jwtService.extractTokenData(jwt).getRol();


        if (rol.equals(Rol.ADMIN.toString())){
            reportService.updateState(changeStateReportDto);
            return;
        }
        throw new NoAdmin("Necesitas ser administrador para esto");
    }

    @GetMapping("/userBanned")
        public List<ReportCountDto> getUserBanned(@RequestParam(value = "offser", defaultValue = "0") int offset){
        return reportService.getUserBanned(offset);
    }

    @GetMapping("/userWarning")
    public List<ReportCountDto> getUserWarning(@RequestParam(value = "offser", defaultValue = "0") int offset){
        return reportService.getUserWarning(offset);
    }
}
