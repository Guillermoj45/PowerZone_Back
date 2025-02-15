package com.actividad_10.powerzone_back.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "groupname")
public class GroupName implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image", nullable = false, length = 200, columnDefinition = "varchar(200) default 'https://default.image.url'")
    private String image = "https://default.image.url";

}
