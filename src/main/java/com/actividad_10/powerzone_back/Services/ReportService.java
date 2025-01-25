package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.DTOs.ReportsAdminDto;
import com.actividad_10.powerzone_back.Repositories.ReportsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ReportService {

    ReportsRepository reportsRepository;


    public List<ReportsAdminDto> getReports(int offset){
        List<ReportsAdminDto> reports = new ArrayList<>();

        reportsRepository.getReports(offset).forEach((reports1) -> {
            reports.add(
                    ReportsAdminDto.builder()
                    .id(reports1.getId())
                    .reason(reports1.getContent())
                    .reporter(reports1.getReporter().getUsername())
                    .reported(reports1.getReported().getUsername())
                    .avatarReported(reports1.getReported().getProfile().getAvatar())
                    .idPost(reports1.getPost().getId())
                    .state(reports1.getType())
                    .build()
            );
        });
        return reports;
    }
}
