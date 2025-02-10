package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.DTOs.Notificaciones.BaseNotification;
import com.actividad_10.powerzone_back.DTOs.Notificaciones.MessageNotification;
import com.actividad_10.powerzone_back.Entities.GroupMessenger;
import com.actividad_10.powerzone_back.Entities.Notification;
import com.actividad_10.powerzone_back.Entities.emun.NotificationType;
import com.actividad_10.powerzone_back.Repositories.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class NotificationService {
    NotificationRepository notificationRepository;
    MessageService messageService;
    UserService userService;

    public List<BaseNotification> getNotification(String token, Long offset) {
        Long userId = userService.returnProfile(token).getId();
        List<Notification> notification = notificationRepository.findNotificationByIdUser(userId, offset);

        List<BaseNotification> baseNotifications = new ArrayList<>();

        notification.forEach(n -> {
            if (n.getType() == NotificationType.MESSAGE) {
                baseNotifications.add(getMessageNotification(n));
            } else if (n.getType() == NotificationType.NEW_COMMENT) {
                // baseNotifications.add(getFriendRequestNotification(n));
            } else if (n.getType() == NotificationType.NEW_FOLLOWER) {
                // baseNotifications.add(getFriendRequestNotification(n));
            } else if (n.getType() == NotificationType.NEW_LIKE) {
                // baseNotifications.add(getFriendRequestNotification(n));
            } else if (n.getType() == NotificationType.NEW_POST) {
                // baseNotifications.add(getFriendRequestNotification(n));
            }
        });
        return baseNotifications;
    }

    @Async
    public MessageNotification getMessageNotification(Notification notification) {
        GroupMessenger groupMessenger = messageService.getMessageById(notification.getContent());
        return new MessageNotification(groupMessenger, notification);
    }
}
