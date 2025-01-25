package com.actividad_10.powerzone_back.DTOs;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReportsAdminDto {
    Integer id;
    String reporter;
    String report;
    String reason;
    Integer idPost;

}
