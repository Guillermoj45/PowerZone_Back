package com.actividad_10.powerzone_back.Entities.Ids;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
public class LikeId implements Serializable {

    // Getters y setters
    private Long postId;
    private Long userId;

    // Constructor por defecto
    public LikeId() {}

    // Constructor con todos los atributos
    public LikeId(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }


    // Sobrescribe equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikeId likeId = (LikeId) o;
        return Objects.equals(postId, likeId.postId) && Objects.equals(userId, likeId.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, userId);
    }
}