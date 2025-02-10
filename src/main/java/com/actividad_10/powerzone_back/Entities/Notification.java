package com.actividad_10.powerzone_back.Entities;

import com.actividad_10.powerzone_back.Entities.emun.NotificationType;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notification")
public class Notification implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "content", nullable = false)
    private Long content;

    @Column(name = "type")
    @Enumerated(EnumType.ORDINAL)
    private NotificationType type;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @JoinColumn(name = "recibe_id", nullable = false)
    @ManyToOne
    private Profile userRecibe;

    @JoinColumn(name = "sender_id", nullable = false)
    @ManyToOne
    private Profile userSend;

}
