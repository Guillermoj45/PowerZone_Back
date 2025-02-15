package com.actividad_10.powerzone_back.Cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
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
    public String uploadFile(Object file, String carpeta) throws IOException {
        Map uploadResult;

        // Caso 1: Si es MultipartFile
        if (file instanceof MultipartFile) {
            MultipartFile multipartFile = (MultipartFile) file;
            if (multipartFile.isEmpty()) {
                throw new RuntimeException("El archivo está vacío");
            }
            uploadResult = cloudinary.uploader().upload(
                    multipartFile.getBytes(),
                    ObjectUtils.asMap("folder", carpeta != null ? "powerzone/" + carpeta : "powerzone")
            );

            // Caso 2: Si es Base64
        } else if (file instanceof String) {
            String base64File = (String) file;


            // Eliminar el prefijo Base64 (data:image/...)
            String base64Data ="";
            try {
                base64Data = base64File.split(",")[1];
            } catch (Exception e) {
                return null;
            }

            // Decodificar el archivo Base64 a bytes
            byte[] fileBytes = Base64.getDecoder().decode(base64Data);

            uploadResult = cloudinary.uploader().upload(
                    fileBytes,
                    ObjectUtils.asMap("folder", carpeta != null ? "powerzone/" + carpeta : "powerzone", "resource_type", "auto")
            );

        } else {
            throw new IllegalArgumentException("El archivo debe ser de tipo MultipartFile o Base64 String");
        }

        // Devolver el public_id o la URL de la imagen subida
        return uploadResult.get("secure_url").toString();
    }
    public UploadResult uploadFilePV(Object file, String carpeta) throws IOException {
        Map uploadResult;

        // Caso 1: Si es MultipartFile
        if (file instanceof MultipartFile) {
            MultipartFile multipartFile = (MultipartFile) file;
            if (multipartFile.isEmpty()) {
                throw new RuntimeException("El archivo está vacío");
            }
            uploadResult = cloudinary.uploader().upload(
                    multipartFile.getBytes(),
                    ObjectUtils.asMap("folder", carpeta != null ? "powerzone/" + carpeta : "powerzone")
            );

            // Caso 2: Si es Base64
        } else if (file instanceof String) {
            String base64File = (String) file;

            // Eliminar el prefijo Base64 (data:image/...)
            String base64Data = base64File.split(",")[1];

            // Decodificar el archivo Base64 a bytes
            byte[] fileBytes = Base64.getDecoder().decode(base64Data);

            uploadResult = cloudinary.uploader().upload(
                    fileBytes,
                    ObjectUtils.asMap("folder", carpeta != null ? "powerzone/" + carpeta : "powerzone", "resource_type", "auto")
            );

        } else {
            throw new IllegalArgumentException("El archivo debe ser de tipo MultipartFile o Base64 String");
        }

        // Devolver el public_id o la URL de la imagen subida y el resource_type
        return new UploadResult(uploadResult.get("public_id").toString(), uploadResult.get("resource_type").toString());
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