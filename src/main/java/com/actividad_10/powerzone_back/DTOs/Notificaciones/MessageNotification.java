package com.actividad_10.powerzone_back.DTOs.Notificaciones;

import com.actividad_10.powerzone_back.Entities.GroupMessenger;
import com.actividad_10.powerzone_back.Entities.Notification;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageNotification extends BaseNotification {
    @JsonProperty
    String message;

    public MessageNotification(GroupMessenger groupMessenger, Notification notification) {
        super(notification);
        this.message = groupMessenger.getMessage();
    }
}
