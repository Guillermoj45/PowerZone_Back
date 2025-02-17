package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.DTOs.ChangeStateReportDto;
import com.actividad_10.powerzone_back.DTOs.CreateReportDTO;
import com.actividad_10.powerzone_back.DTOs.ReportCountDto;
import com.actividad_10.powerzone_back.DTOs.ReportsAdminDto;
import com.actividad_10.powerzone_back.Entities.Post;
import com.actividad_10.powerzone_back.Entities.Report;
import com.actividad_10.powerzone_back.Entities.User;
import com.actividad_10.powerzone_back.Entities.emun.ReportState;
import com.actividad_10.powerzone_back.Repositories.ReportsRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ReportService implements IReportService {

    private final ProfileService profileService;
    private final ReportsRepository reportsRepository;
    private final PostService postService;
    private final UserService userService;

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
                    .reporter(reports1.getReporter().getProfile().getNickname())
                    .reported(reports1.getPost().getUser().getProfile().getNickname())
                    .avatarReported(reports1.getPost().getUser().getProfile().getAvatar())
                    .idPost(reports1.getPost().getId())
                    .state(reports1.getType())
                    .build()
            );
        });
        return reports;
    }

    /**
     * Actualiza el estado de un reporte
     * @param dto dto con el id del reporte y el estado al que se cambiara
     * @return 1 si se actualizo correctamente
     */
    @Override
    public Integer updateState(ChangeStateReportDto dto) {

        List<Report> report = reportsRepository.findByPost_Id(dto.getId());
        report.forEach((report1) -> {
            new Thread(() -> {
                report1.setType(dto.getState());
                reportsRepository.save(report1);
            }).start();
        });

        Post post = postService.findaById(dto.getId());
        if (dto.getState() == ReportState.SANCTIONED){
            post.setDelete(true);

        } else {
            post.setDelete(false);

        }
        if (reportsRepository.countReports(post.getUser().getId()) >= 5){
            post.getUser().getProfile().setBanAt(LocalDate.now().plusDays(7));
            profileService.save(post.getUser().getProfile());
        } else {
            post.getUser().getProfile().setBanAt(null);
            profileService.save(post.getUser().getProfile());
        }
        System.out.println(postService.updatePost(post));
        return 1;
    }

    /**
     * Toma 50 usuarios advertidos
     *
     * @param offset desde donde se tomaran los usuarios
     * @return los siguientes 50 usuarios advertidos
     */
    @Override
    public List<ReportCountDto> getUserWarning(int offset) {
        return reportsRepository.userWarning(offset, ReportState.SANCTIONED);
    }

    /**
     * Toma 50 usuarios baneados
     *
     * @param offset desde donde se tomaran los usuarios
     * @return los siguientes 50 usuarios baneados
     */
    @Override
    public List<ReportCountDto> getUserBanned(int offset) {
        return reportsRepository.userBanner(offset, ReportState.SANCTIONED);
    }


    @Transactional
    public void reportPost(CreateReportDTO report, String emailUsername) {
        Post post = postService.findaById(report.getPostId());

        User reporter = (User) userService.loadUserByUsername(emailUsername);

        Report newReport = Report.builder()
                .content(report.getContend())
                .createdAtPost(post.getCreatedAt())
                .post(post)
                .reporter(reporter)
                .type(ReportState.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        reportsRepository.save(newReport);
    }
}
