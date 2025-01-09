package com.actividad_10.powerzone_back.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "follower")
public class Follower implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "follower_id", nullable = false)
    private Long followerId;

}
