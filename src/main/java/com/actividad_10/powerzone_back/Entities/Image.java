package com.actividad_10.powerzone_back.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "image")
public class Image implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "post_created_at", nullable = false)
    private LocalDateTime postCreatedAt;

    @ManyToOne
    @JoinColumn(name = "id_post", nullable = false)
    private Post post;
}