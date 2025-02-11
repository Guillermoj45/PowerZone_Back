package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.DTOs.CommentDto;
import com.actividad_10.powerzone_back.DTOs.Notificaciones.MegaNotificacion;
import com.actividad_10.powerzone_back.DTOs.PostDto;
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
    private final NotificationRepository notificationRepository;
    private final MessageService messageService;
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;


    public List<MegaNotificacion> getNotification(String token, Long offset) {
        Long userId = userService.returnProfile(token).getId();
        List<Notification> notification = notificationRepository.findNotificationByIdUser(userId, offset);

        List<MegaNotificacion> megaNotificacions = new ArrayList<>();

        notification.forEach(n -> {
            if (n.getType() == NotificationType.MESSAGE) {
                megaNotificacions.add(getFriendRequestNotification(n));
            } else if (n.getType() == NotificationType.NEW_COMMENT) {
                megaNotificacions.add(getCommentNotification(n));
            } else if (n.getType() == NotificationType.NEW_FOLLOWER) {
                megaNotificacions.add(getLikeAndFollowNotification(n));
            } else if (n.getType() == NotificationType.NEW_LIKE) {
                megaNotificacions.add(getLikeAndFollowNotification(n));
            } else if (n.getType() == NotificationType.NEW_POST) {
                megaNotificacions.add(getNewPostNotification(n));
            }
        });
        return megaNotificacions;
    }

    // @Async
    // public MessageNotification getMessageNotification(Notification notification) {
    //     GroupMessenger groupMessenger = messageService.getMessageById(notification.getContent());
    //     return new MessageNotification(groupMessenger, notification);
    // }

    @Async
    public MegaNotificacion getFriendRequestNotification(Notification notification) {
        GroupMessenger groupMessenger = messageService.getMessageById(notification.getContent());

        return new MegaNotificacion(notification, groupMessenger);
    }

    @Async
    public MegaNotificacion getNewPostNotification(Notification notification) {
        PostDto postDto = postService.getPostById(notification.getContent());

        return new MegaNotificacion(notification, postDto);
    }

    @Async
    public MegaNotificacion getLikeAndFollowNotification(Notification notification) {
        PostDto likePost = postService.getPostById(notification.getContent());

        return new MegaNotificacion(notification, likePost);
    }

    @Async
    public MegaNotificacion getCommentNotification(Notification notification) {
        CommentDto comment = commentService.getComentarioById(notification.getContent());
        PostDto postDto = postService.getPostById(comment.getPostId());

        return new MegaNotificacion(notification, comment,postDto);
    }
}
