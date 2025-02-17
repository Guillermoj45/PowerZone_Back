package com.actividad_10.powerzone_back.Controllers;

import com.actividad_10.powerzone_back.Config.JwtService;
import com.actividad_10.powerzone_back.DTOs.ChangeStateReportDto;
import com.actividad_10.powerzone_back.DTOs.ReportCountDto;
import com.actividad_10.powerzone_back.DTOs.ReportsAdminDto;
import com.actividad_10.powerzone_back.Entities.emun.Rol;
import com.actividad_10.powerzone_back.Exceptions.NoAdmin;
import com.actividad_10.powerzone_back.Services.ReportService;
import com.actividad_10.powerzone_back.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private ReportService reportService;
    private JwtService jwtService;
    private UserService userService;

    @GetMapping()
    public List<ReportsAdminDto> getReports(@RequestHeader("Authorization") String token, @RequestParam(value = "offset", defaultValue = "0") int offset, Principal principal){
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
        public List<ReportCountDto> getUserBanned(@RequestHeader("Authorization") String token,@RequestParam(value = "offser", defaultValue = "0") int offset){
        String jwt = token.replace("Bearer ", "");
        String rol = jwtService.extractTokenData(jwt).getRol();


        if (rol.equals(Rol.ADMIN.toString())){
            return reportService.getUserBanned(offset);
        }
        throw new NoAdmin("Necesitas ser administrador para esto");
    }

    @GetMapping("/userWarning")
    public List<ReportCountDto> getUserWarning(@RequestHeader("Authorization") String token, @RequestParam(value = "offser", defaultValue = "0") int offset){
        String jwt = token.replace("Bearer ", "");
        String rol = jwtService.extractTokenData(jwt).getRol();


        if (rol.equals(Rol.ADMIN.toString())){
            return reportService.getUserWarning(offset);
        }

        throw new NoAdmin("Necesitas ser administrador para esto");
    }

    @GetMapping("/isAdmin")
    public ResponseEntity<Boolean> isAdmin(@RequestHeader("Authorization") String token){
        return new ResponseEntity<>(userService.isAdmin(token), HttpStatus.OK);
    }
}
