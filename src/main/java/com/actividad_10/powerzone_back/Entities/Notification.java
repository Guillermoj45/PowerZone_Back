package com.actividad_10.powerzone_back.Entities;

import com.actividad_10.powerzone_back.Entities.emun.NotificationType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notification")
@NoArgsConstructor
public class Notification implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    /**
     * Constructor para mensajes
     *
     * @param groupMessenger el mensaje en cuestión
     * @param profile1 el perfil al que a sido enviado el mensaje
     */
    public Notification(GroupMessenger groupMessenger, Profile profile1) {
        this.content = groupMessenger.getId();
        this.userRecibe = profile1;
        this.userSend = groupMessenger.getGrupouser().getUser().getProfile();
        this.type = NotificationType.MESSAGE;
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString(){
        return "Notification{" +
                "id=" + id +
                ", content=" + content +
                ", type=" + type +
                ", createdAt=" + createdAt +
                ", userRecibe=" + userRecibe.getNickname() +
                ", userSend=" + userSend.getNickname() +
                '}';
    }

}
