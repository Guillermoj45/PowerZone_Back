package com.actividad_10.powerzone_back.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Table(name = "profile")
public class Profile implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    //Nombre de usuario
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "avatar", nullable = false)
    private String avatar;

    //Fecha de nacimiento
    @Column(name = "born_date", nullable = false)
    private LocalDate bornDate;

    @Column(name = "ban_at")
    private Date banAt;

    @Column(name = "diet")
    private Long diet;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

}
