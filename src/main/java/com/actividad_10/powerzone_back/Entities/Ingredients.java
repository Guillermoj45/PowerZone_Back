package com.actividad_10.powerzone_back.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "ingredients")
public class Ingredients implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToMany
    @JoinTable(name = "alimentacion_ingredients",
            joinColumns = {@JoinColumn(name = "ingredients_id")},
            inverseJoinColumns = {@JoinColumn(name = "alimentacion_id")})
    private Set<Alimentacion> alimentacions;

}
