package com.actividad_10.powerzone_back.Entities;

import com.actividad_10.powerzone_back.Entities.emun.NotificationType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
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

    /**
     * Constructor para notificaciones de seguimiento
     * @param profile el perfil que sigue
     * @param profile1 el perfil que es seguido
     */
    public Notification(Profile profile, Profile profile1) {
        this.userSend = profile;
        this.userRecibe = profile1;
        this.content = profile.getId();
        this.type = NotificationType.NEW_FOLLOWER;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Constructor para notificaciones de posts
     * @param profile1 el perfil al que se le envía la notificación
     * @param post el post en cuestión
     */
    public Notification(Post post,Profile profile1) {
        this.userSend = post.getUser().getProfile();
        this.userRecibe = profile1;
        this.content = post.getId();
        this.type = NotificationType.NEW_POST;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Constructor para notificaciones de comentarios
     * @param post el post al que se le ha comentado
     * @param profile1 el perfil al que se le envía la notificación
     */
    public Notification(Post post, Profile profile1, NotificationType type) {
        this.userSend = profile1;
        this.userRecibe = post.getUser().getProfile();
        this.content = post.getId();
        this.type = type;
        this.createdAt = LocalDateTime.now();
    }

        /**
     * Constructor para notificaciones de comentarios
     * @param post el post al que se le ha comentado
     * @param profile1 el perfil al que se le envía la notificación
     */
    public Notification(Post post, Profile profile1, Comment comment) {
        this.userSend = profile1;
        this.userRecibe = post.getUser().getProfile();
        this.content = comment.getId();
        this.type = NotificationType.NEW_COMMENT;
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
