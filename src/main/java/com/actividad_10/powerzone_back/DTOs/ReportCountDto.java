package com.actividad_10.powerzone_back.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ReportCountDto {
    // Getters and setters
    private String name;
    private String avatar;
    private Long reportsCount;

}
