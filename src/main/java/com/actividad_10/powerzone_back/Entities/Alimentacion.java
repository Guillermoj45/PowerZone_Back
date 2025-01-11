package com.actividad_10.powerzone_back.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "alimentacion")
public class Alimentacion implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToMany
    @JoinTable(name = "diet_alimentacion",
        joinColumns = {@JoinColumn(name = "alimentacion_id")},
        inverseJoinColumns = {@JoinColumn(name = "diet_id")})
    private Set<Diet> diets;

}
