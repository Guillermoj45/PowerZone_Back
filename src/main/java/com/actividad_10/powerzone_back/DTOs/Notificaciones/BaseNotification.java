package com.actividad_10.powerzone_back.DTOs.Notificaciones;

import com.actividad_10.powerzone_back.DTOs.Profile2Dto;
import com.actividad_10.powerzone_back.Entities.Notification;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDateTime;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = MessageNotification.class, name = "Message"),
    // @JsonSubTypes.Type(value = SubclassB.class, name = "B")
})
public class BaseNotification {
    @JsonProperty
    private Profile2Dto receiver;
    @JsonProperty
    private Profile2Dto emitter;
    @JsonProperty
    private LocalDateTime date;

    public BaseNotification(Notification notification) {
        this.receiver = new Profile2Dto(notification.getUserRecibe());
        this.emitter = new Profile2Dto(notification.getUserSend());
        this.date = notification.getCreatedAt();
    }
}
