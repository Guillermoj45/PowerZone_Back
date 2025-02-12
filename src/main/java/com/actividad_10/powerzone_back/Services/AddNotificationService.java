package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.Entities.GroupMessenger;
import com.actividad_10.powerzone_back.Entities.Notification;
import com.actividad_10.powerzone_back.Entities.Profile;
import com.actividad_10.powerzone_back.Entities.emun.NotificationType;
import com.actividad_10.powerzone_back.Repositories.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AddNotificationService {
    NotificationRepository notificationRepository;

        @Async
    public void createNotificationMessaje(GroupMessenger groupMessenger) {
        Notification notification = new Notification();
        Profile profile = groupMessenger.getGrupouser().getUser().getProfile();
        notification.setUserSend(profile);
        notification.setType(NotificationType.MESSAGE);
        notificationRepository.save(notification);
    }

}
