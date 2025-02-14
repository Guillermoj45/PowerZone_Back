package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.DTOs.Notificaciones.MegaNotificacion;
import com.actividad_10.powerzone_back.DTOs.PostDto;
import com.actividad_10.powerzone_back.DTOs.Profile2Dto;
import com.actividad_10.powerzone_back.Entities.GroupMessenger;
import com.actividad_10.powerzone_back.Entities.Notification;
import com.actividad_10.powerzone_back.Entities.Post;
import com.actividad_10.powerzone_back.Entities.Profile;
import com.actividad_10.powerzone_back.Entities.emun.NotificationType;
import com.actividad_10.powerzone_back.Repositories.NotificationRepository;
import com.actividad_10.powerzone_back.Repositories.ProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AddNotificationService {
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ProfileRepository profileRepository;

    @Async
    public void createNotificationMessaje(GroupMessenger groupMessenger) {
        List<Profile> profiles = notificationRepository.getUserGroupUser(groupMessenger.getGrupouser().getGroup().getId());

        for (Profile profile1 : profiles) {
            if (!profile1.getUser().getId().equals(groupMessenger.getGrupouser().getUser().getId())) {
                Notification notification = new Notification(groupMessenger, profile1);
                System.out.println("Mensaje enviado a: " + profile1.getUser().getId());

                MegaNotificacion notification1 = new MegaNotificacion(notification, groupMessenger);
                sendNotification(notification1);

                saveNotification(notification);
            }
        }
    }

    @Async
    public void createNotificationNewPost(Post post) {
        for (Profile profile1 : profileRepository.findProfileWithFollowing(post.getUser().getId())) {
            Notification notification = new Notification(post, profile1, NotificationType.NEW_POST);

            MegaNotificacion notification1 = new MegaNotificacion(notification, new PostDto(post));
            sendNotification(notification1);

            saveNotification(notification);
        }
    }

    @Async
    public void createNotificationFollow(Profile profile, Profile profile1) {
        Notification notification = new Notification(profile, profile1);

        MegaNotificacion notification1 = new MegaNotificacion(notification, new Profile2Dto(profile));
        sendNotification(notification1);

        saveNotification(notification);
    }

    @Async
    public void createNotificationLike(Post post, Profile profile1) {
        Notification notification = new Notification(post, profile1, NotificationType.NEW_LIKE);

        MegaNotificacion notification1 = new MegaNotificacion(notification, new PostDto(post));
        sendNotification(notification1);

        saveNotification(notification);
    }

    @Async
    public void createNotificationComment(Post post, Profile profile1) {
        Notification notification = new Notification(post, profile1, NotificationType.NEW_COMMENT);

        MegaNotificacion notification1 = new MegaNotificacion(notification, new PostDto(post));

        saveNotification(notification);
    }

    @Async
    public void sendNotification(MegaNotificacion notification) {
        messagingTemplate.convertAndSend("/topic/roomNotification/25", notification);
    }

    @Async
    protected void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }


}
