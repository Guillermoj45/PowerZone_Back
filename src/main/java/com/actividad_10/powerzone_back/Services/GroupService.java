package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.Entities.GroupName;
import com.actividad_10.powerzone_back.Repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    // Método para obtener un grupo por su ID
    public GroupName getGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElse(null);  // Devuelve el grupo o null si no se encuentra
    }
}
