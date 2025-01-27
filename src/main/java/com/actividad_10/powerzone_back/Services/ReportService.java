package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.DTOs.ReportsAdminDto;
import com.actividad_10.powerzone_back.Entities.User;
import com.actividad_10.powerzone_back.Entities.emun.ReportState;
import com.actividad_10.powerzone_back.Repositories.ReportsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ReportService implements IReportService {

    private ReportsRepository reportsRepository;


    /**
     * Toma 50 reportes de la base de datos
     * @param offset desde donde se tomaran los reportes
     * @return los siguientes 50 reportes
     */
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

    /**
     * Actualiza el estado de un reporte
     * @param id del reporte
     * @param state nuevo estado
     * @return 1 si se actualizo correctamente
     */
    @Override
    public Integer updateState(int id, ReportState state) {
        return reportsRepository.updateState(id, state);
    }

    /**
     * Toma 50 usuarios advertidos
     * @param offset desde donde se tomaran los usuarios
     * @return los siguientes 50 usuarios advertidos
     */
    @Override
    public List<User> getUserWarning(int offset) {
        return reportsRepository.userWarning(offset, ReportState.SANCTIONED);
    }

    /**
     * Toma 50 usuarios baneados
     * @param offset desde donde se tomaran los usuarios
     * @return los siguientes 50 usuarios baneados
     */
    @Override
    public List<User> getUserBanned(int offset) {
        return List.of();
    }


}
