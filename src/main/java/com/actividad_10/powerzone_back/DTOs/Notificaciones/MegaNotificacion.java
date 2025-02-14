package com.actividad_10.powerzone_back.DTOs.Notificaciones;

import com.actividad_10.powerzone_back.DTOs.CommentDto;
import com.actividad_10.powerzone_back.DTOs.PostDto;
import com.actividad_10.powerzone_back.DTOs.Profile2Dto;
import com.actividad_10.powerzone_back.Entities.GroupMessenger;
import com.actividad_10.powerzone_back.Entities.Notification;
import com.actividad_10.powerzone_back.Entities.emun.NotificationType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
public class MegaNotificacion {

    // datos basicos
    private Long id;
    private Profile2Dto receiver;
    private Profile2Dto emitter;
    private LocalDateTime date;
    private NotificationType type;

    // mensajes
    private GroupMessenger groupMessenger;

    // post Like
    private PostDto postDto;

    // follow
    private Profile2Dto profile;

    // comment
    private CommentDto comment;

    /**
     * Constructor para notificaciones de tipo mensaje
     * @param notification la notificación en cuestión
     * @param groupMessenger el mensaje al que hace referencia la notificación
     */
    public MegaNotificacion(Notification notification, GroupMessenger groupMessenger) {
        this.id = notification.getId();
        this.receiver = new Profile2Dto(notification.getUserRecibe());
        this.emitter = new Profile2Dto(notification.getUserSend());
        this.date = notification.getCreatedAt();
        this.type = notification.getType();
        this.groupMessenger = groupMessenger;
    }

    /**
     * Constructor para notificaciones de tipo follow
     * @param notification la notificación en cuestión
     * @param profile el perfil al que hace referencia la notificación
     */
    public MegaNotificacion(Notification notification, Profile2Dto profile) {
        this.id = notification.getId();
        this.receiver = new Profile2Dto(notification.getUserRecibe());
        this.emitter = new Profile2Dto(notification.getUserSend());
        this.date = notification.getCreatedAt();
        this.type = notification.getType();
        this.profile = new Profile2Dto(notification.getUserSend());
    }

    /**
     * Constructor para notificaciones de tipo like y nuevo post
     * @param notification la notificación en cuestión
     * @param postDto el post al que hace referencia la notificación
     */
    public MegaNotificacion(Notification notification, PostDto postDto) {
        this.id = notification.getId();
        this.receiver = new Profile2Dto(notification.getUserRecibe());
        this.emitter = new Profile2Dto(notification.getUserSend());
        this.date = notification.getCreatedAt();
        this.type = notification.getType();
        this.postDto = postDto;
    }


    /**
     * Constructor para notificaciones de tipo follow
     * @param notification la notificación en cuestión
     * @param comment el comentario al que hace referencia la notificación
     */
    public MegaNotificacion(Notification notification, CommentDto comment, PostDto postDto) {
        this.id = notification.getId();
        this.receiver = new Profile2Dto(notification.getUserRecibe());
        this.emitter = new Profile2Dto(notification.getUserSend());
        this.date = notification.getCreatedAt();
        this.type = notification.getType();
        this.comment = comment;
        this.postDto = postDto;
    }
}
