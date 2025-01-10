package com.actividad_10.powerzone_back.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "groupuser")
@IdClass(Groupuser.class)
public class Groupuser implements Serializable {

    @Column(name = "id", nullable = false)
    private Long id;

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "group_id", nullable = false)
    private Long groupId;

}
