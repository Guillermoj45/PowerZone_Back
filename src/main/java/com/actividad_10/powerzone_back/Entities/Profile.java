package com.actividad_10.powerzone_back.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@Table(name = "profile")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Profile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "avatar", nullable = false, length = 200, columnDefinition = "varchar(200) default 'https://res.cloudinary.com/dflz0gveu/image/upload/v1718394870/avatars/default.png'")
    private String avatar = "https://res.cloudinary.com/dflz0gveu/image/upload/v1718394870/avatars/default.png";

    @Column(name = "born_date", nullable = false)
    private LocalDate bornDate;

    @Column(name = "ban_at")
    private LocalDate banAt;

    @Column(name = "is_new_user", nullable = false, columnDefinition = "boolean default true")
    private boolean isNewUser = true;

    @Column(name = "diet")
    private Long diet;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt = LocalDate.now();

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @JsonIgnore
    @OneToOne
    @MapsId
    @JoinColumn(name = "id", referencedColumnName = "id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "follower",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    @JsonManagedReference
    private Set<Profile> followers;

    @ManyToMany(mappedBy = "followers")
    @JsonBackReference
    private Set<Profile> following;

}
