package com.actividad_10.powerzone_back.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "profile")
public class Profile implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id", referencedColumnName = "id")
    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "avatar", nullable = false, length = 200, columnDefinition = "varchar(200) default 'https://res.cloudinary.com/dflz0gveu/image/upload/v1718394870/avatars/default.png'")
    private String avatar = "https://res.cloudinary.com/dflz0gveu/image/upload/v1718394870/avatars/default.png";

    @Column(name = "born_date", nullable = false)
    private LocalDate bornDate;

    @Column(name = "ban_at")
    private LocalDate banAt;

    @Column(name = "diet")
    private Long diet;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt = LocalDate.now();

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}
