package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.DTOs.ChangeStateReportDto;
import com.actividad_10.powerzone_back.DTOs.ReportCountDto;
import com.actividad_10.powerzone_back.DTOs.ReportsAdminDto;

import java.util.List;

public interface IReportService {

    List<ReportsAdminDto> getReports(int offset);

    Integer updateState(ChangeStateReportDto dto);

    List<ReportCountDto> getUserWarning(int offset);

    List<ReportCountDto> getUserBanned(int offset);

}
