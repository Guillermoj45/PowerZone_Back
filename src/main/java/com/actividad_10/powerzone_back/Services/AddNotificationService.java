package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.Entities.GroupMessenger;
import com.actividad_10.powerzone_back.Entities.Notification;
import com.actividad_10.powerzone_back.Entities.Post;
import com.actividad_10.powerzone_back.Entities.Profile;
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
                // messagingTemplate.convertAndSend("/notification/" + profile1.getId(), notification);
                System.out.println(notification);
                notificationRepository.save(notification);
            }

        }
    }

    @Async
    public void createNotificationNewPost(Post post) {
        for (Profile profile1 : profileRepository.findProfileWithFollowing(post.getUser().getId())) {
            Notification notification = new Notification(post, profile1);
            System.out.println(notification);
            notificationRepository.save(notification);
        }
    }
}
