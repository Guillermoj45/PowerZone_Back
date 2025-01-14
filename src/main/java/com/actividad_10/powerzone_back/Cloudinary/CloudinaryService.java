package com.actividad_10.powerzone_back.Cloudinary;

import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    /**
     * Sube un archivo a Cloudinary
     * @param file Archivo a subir
     * @param carpeta Carpeta en la que se guardará el archivo
     * @return URL pública del archivo subido
     * @throws IOException Si ocurre un error al subir el archivo
     */
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
        return uploadResult.get("public_id").toString();
    }

    /**
     * Eliminar una imagen de Cloudinary.
     * @param publicId El public_id de la imagen a eliminar.
     * @return Un mapa con el resultado de la operación.
     * @throws Exception Sí ocurre algún error durante la operación.
     */
    public Map deleteImage(String publicId) throws Exception {
        return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}