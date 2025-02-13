package com.actividad_10.powerzone_back.DTOs;

import com.actividad_10.powerzone_back.Entities.emun.ReportState;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeStateReportDto {
    private Long id;
    private ReportState state;
}
