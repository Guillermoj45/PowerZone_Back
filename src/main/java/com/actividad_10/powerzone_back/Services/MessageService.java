package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.DTOs.ChatMessage;
import com.actividad_10.powerzone_back.Entities.GroupMessenger;
import com.actividad_10.powerzone_back.Entities.GroupName;
import com.actividad_10.powerzone_back.Entities.GroupUser;
import com.actividad_10.powerzone_back.Entities.Profile;
import com.actividad_10.powerzone_back.Repositories.GroupMessengerRepository;
import com.actividad_10.powerzone_back.Repositories.GroupNameRepository;
import com.actividad_10.powerzone_back.Repositories.GroupUserRepository;
import jdk.dynalink.linker.LinkerServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@AllArgsConstructor
public class MessageService {
    private final GroupMessengerRepository groupMessengerRepository;
    private final GroupUserRepository groupUserRepository;
    private final GroupNameRepository groupNameRepository;
    private final ProfileService profileService;
    private final AddNotificationService addNotificationService;

    public GroupMessenger getMessageById(Long id){
        return groupMessengerRepository.findById(id).orElse(null);
    }

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

}