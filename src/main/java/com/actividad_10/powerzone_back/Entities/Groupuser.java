package com.actividad_10.powerzone_back.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "groupuser")

public class Groupuser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;


    @Column(name = "group_id", nullable = false)
    private Long groupId;

    public Groupuser() {

    }

    public Groupuser(Long userId, Long groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }


}
