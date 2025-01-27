package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.DTOs.ReportsAdminDto;
import com.actividad_10.powerzone_back.Entities.User;
import com.actividad_10.powerzone_back.Entities.emun.ReportState;

import java.util.List;

public interface IReportService {

    List<ReportsAdminDto> getReports(int offset);

    Integer updateState(int id, ReportState state);

    List<User> getUserWarning(int offset);

    List<User> getUserBanned(int offset);

}
