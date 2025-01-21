package com.actividad_10.powerzone_back.Entities.Ids;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Setter
@Getter
public class PostId implements Serializable {

    // Getters y setters
    private Long id;
    private LocalDate createdAt;

    // Constructor por defecto
    public PostId() {}

    // Constructor con todos los atributos
    public PostId(Long id, LocalDate createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    // Sobrescribe equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostId postId = (PostId) o;
        return Objects.equals(id, postId.id) && Objects.equals(createdAt, postId.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt);
    }
}

