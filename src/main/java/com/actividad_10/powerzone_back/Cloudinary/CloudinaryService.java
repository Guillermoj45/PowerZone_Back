package com.actividad_10.powerzone_back.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(MultipartFile file, String carpeta) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("El archivo está vacío");
        }
        Map uploadResult;
        if (carpeta == null) {
            uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "powerzone"));

        } else {
            uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "powerzone/" + carpeta));
        }
        return uploadResult.get("url").toString(); // Retorna la URL pública del archivo
    }
}
