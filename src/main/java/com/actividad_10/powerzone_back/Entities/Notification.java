package com.actividad_10.powerzone_back.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "notification")
public class Notification implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "type")
    private Integer type;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "user_id", nullable = false)
    private Long userId;

}
