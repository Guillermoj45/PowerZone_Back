package com.actividad_10.powerzone_back.Entities.Ids;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Setter
@Getter
public class ImageId implements Serializable {

    // Getters y setters
    private Long id;
    private Date postCreatedAt;
    private String image;

    // Constructor por defecto
    public ImageId() {}

    // Constructor con todos los atributos
    public ImageId(Long id, Date postCreatedAt, String image) {
        this.id = id;
        this.postCreatedAt = postCreatedAt;
        this.image = image;
    }

    // Sobrescribe equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageId imageId = (ImageId) o;
        return Objects.equals(id, imageId.id) &&
               Objects.equals(postCreatedAt, imageId.postCreatedAt) &&
               Objects.equals(image, imageId.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, postCreatedAt, image);
    }
}
