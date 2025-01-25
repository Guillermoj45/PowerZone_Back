package com.actividad_10.powerzone_back.DTOs;

import com.actividad_10.powerzone_back.Entities.emun.ReportState;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReportsAdminDto {
    Long id;
    String reporter;
    String reported;
    String avatarReported;
    String reason;
    Long idPost;
    ReportState state;
}
