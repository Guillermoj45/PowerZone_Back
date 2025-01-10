package com.actividad_10.powerzone_back.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    //Correo electronico
    @Column(name = "email", nullable = false)
    private String email;

    //Contraseña
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private Integer role;

    @ManyToMany
    @JoinTable(name = "follower",
        joinColumns = {@JoinColumn(name = "user_id")},
        inverseJoinColumns = {@JoinColumn(name = "follower_id")}
    )
    private Set<User> users;
}
