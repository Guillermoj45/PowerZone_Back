package com.actividad_10.powerzone_back.Cloudinary;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadResult {
    private String publicId;
    private String resourceType;

}
