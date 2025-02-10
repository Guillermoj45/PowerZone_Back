package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.DTOs.ChatMessage;
import com.actividad_10.powerzone_back.Entities.GroupMessenger;
import com.actividad_10.powerzone_back.Entities.GroupName;
import com.actividad_10.powerzone_back.Entities.GroupUser;
import com.actividad_10.powerzone_back.Repositories.GroupMessengerRepository;
import com.actividad_10.powerzone_back.Repositories.GroupNameRepository;
import com.actividad_10.powerzone_back.Repositories.GroupUserRepository;
import jdk.dynalink.linker.LinkerServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {
    private GroupMessengerRepository groupMessengerRepository;
    private GroupUserRepository groupUserRepository;
    private GroupNameRepository groupNameRepository;

    public void saveMessage(ChatMessage chatMessage, String GrupoName){
        GroupName groupName = groupNameRepository.findById(chatMessage.getGroupId()).orElse(null);
        if (groupName == null){
            groupName = new GroupName();
            groupName.setName(GrupoName);
            groupName = groupNameRepository.save(groupName);
        }
        GroupUser groupUser = groupUserRepository.findByUserIdAndGroupId(chatMessage.getUserId(), chatMessage.getGroupId());

        if(groupUser == null){
            groupUser = new GroupUser();
            groupUser.setUserId(chatMessage.getUserId());
            groupUser.setGroupId(chatMessage.getGroupId());
            groupUser = groupUserRepository.save(groupUser);
        }

        GroupMessenger groupMessenger = new GroupMessenger();
        groupMessenger.setMessage(chatMessage.getContent());
        groupMessenger.setGrupouser(groupUser);
        groupMessenger.setCreatedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(chatMessage.getTimestamp())), ZoneId.systemDefault()));

        groupMessengerRepository.save(groupMessenger);
    }

    
    public List<GroupName> getGroups(Long userId){
        return groupNameRepository.getGroupsByUserId(userId);
    }

    public List<GroupMessenger> getMessages(Long groupId){
        return groupMessengerRepository.findByGrupouser_GroupId(groupId);
    }
}