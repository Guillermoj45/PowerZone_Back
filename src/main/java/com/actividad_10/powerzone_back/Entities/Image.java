package com.actividad_10.powerzone_back.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "image")
@IdClass(Image.class)
public class Image implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Id
    @Column(name = "post_created_at", nullable = false)
    private Date postCreatedAt;

    @Id
    @Column(name = "image", nullable = false)
    private String image;

}
