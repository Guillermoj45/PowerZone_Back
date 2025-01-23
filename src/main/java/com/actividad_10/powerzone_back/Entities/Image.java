package com.actividad_10.powerzone_back.Entities;

import com.actividad_10.powerzone_back.Entities.emun.MultimediaType;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "image")
public class Image implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_post")
    private Long idPost;

    @Column(name = "post_created_at", nullable = false)
    private LocalDate postCreatedAt;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private MultimediaType type = MultimediaType.IMAGE;

    @Column(name = "image", nullable = false)
    private String image;

}
