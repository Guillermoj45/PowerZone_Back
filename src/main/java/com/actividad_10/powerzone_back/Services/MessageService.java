package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.DTOs.ChatMessage;
import com.actividad_10.powerzone_back.DTOs.GrupoUltimoMensajeDTO;
import com.actividad_10.powerzone_back.Entities.GroupMessenger;
import com.actividad_10.powerzone_back.Entities.GroupName;
import com.actividad_10.powerzone_back.Entities.GroupUser;
import com.actividad_10.powerzone_back.Entities.Profile;
import com.actividad_10.powerzone_back.Repositories.ChatMessageRepository;
import com.actividad_10.powerzone_back.Repositories.GroupMessengerRepository;
import com.actividad_10.powerzone_back.Repositories.GroupNameRepository;
import com.actividad_10.powerzone_back.Repositories.GroupUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {
    private final GroupMessengerRepository groupMessengerRepository;
    private final GroupUserRepository groupUserRepository;
    private final GroupNameRepository groupNameRepository;
    private final ProfileService profileService;
    private final AddNotificationService addNotificationService;
    private final ChatMessageRepository chatMessageRepository;

    public GroupMessenger getMessageById(Long id){
        return groupMessengerRepository.findById(id).orElse(null);
    }

    @Async
    public void saveMessage(ChatMessage chatMessage, String GrupoName){
        GroupName groupName = groupNameRepository.findById(chatMessage.getGroupId()).orElse(null);
        if (groupName == null){
            groupName = new GroupName();
            groupName.setName(GrupoName);
            groupName = groupNameRepository.save(groupName);
        }
        GroupUser groupUser = groupUserRepository.findByUserIdAndGroupId(chatMessage.getUserId(), chatMessage.getGroupId());
        Profile profile = profileService.getProfileById(chatMessage.getUserId());

        if(groupUser == null){
            groupUser = new GroupUser();
            groupUser.setUser(profile.getUser());
            groupUser.setGroup(groupName);
            groupUser = groupUserRepository.save(groupUser);
        }

        GroupMessenger groupMessenger = new GroupMessenger();
        groupMessenger.setMessage(chatMessage.getContent());
        groupMessenger.setGrupouser(groupUser);
        groupMessenger.setCreatedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(chatMessage.getTimestamp())), ZoneId.systemDefault()));

        groupMessenger = groupMessengerRepository.save(groupMessenger);
        addNotificationService.createNotificationMessaje(groupMessenger);
    }

    
    public List<GroupName> getGroups(Long userId){
        return groupNameRepository.getGroupsByUserId(userId);
    }

    public List<GroupMessenger> getMessages(Long groupId){
        return groupMessengerRepository.findByGrupouser_GroupId(groupId);
    }

    public List<GrupoUltimoMensajeDTO> obtenerUltimosMensajesPorGrupo() {
        return chatMessageRepository.obtenerUltimosMensajesPorGrupo();
    }

}