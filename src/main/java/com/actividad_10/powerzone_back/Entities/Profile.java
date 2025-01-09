package com.actividad_10.powerzone_back.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "profile")
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "avatar", nullable = false)
    private String avatar;

    @Column(name = "born_date", nullable = false)
    private Date bornDate;

    @Column(name = "ban_at")
    private Date banAt;

    @Column(name = "diet")
    private Long diet;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

}
