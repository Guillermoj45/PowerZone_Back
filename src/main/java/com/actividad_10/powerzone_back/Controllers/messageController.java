package com.actividad_10.powerzone_back.Controllers;

import com.actividad_10.powerzone_back.DTOs.ChatMessage;
import com.actividad_10.powerzone_back.Entities.GroupMessenger;
import com.actividad_10.powerzone_back.Entities.Groupname;
import com.actividad_10.powerzone_back.Entities.Groupuser;
import com.actividad_10.powerzone_back.Repositories.GroupmessengerRepository;
import com.actividad_10.powerzone_back.Repositories.GroupnameRepository;
import com.actividad_10.powerzone_back.Repositories.GroupuserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/messages")
public class messageController {

    @Autowired
    private GroupmessengerRepository groupMessengerRepository;

    @Autowired
    private GroupuserRepository groupUserRepository;

    @Autowired
    private GroupnameRepository groupNameRepository;

    // Manejar mensajes enviados por los clientes
    @MessageMapping("/chat/{roomId}") // Los clientes envían mensajes a /app/chat
    @SendTo("/topic/messages/{roomId}") // Los mensajes se envían a los suscriptores de /topic/messages
    public ChatMessage send(@DestinationVariable String roomId, ChatMessage message) {
        // Aquí puedes guardar el mensaje en la base de datos si es necesario
        message.setTimestamp(System.currentTimeMillis());
        return message; // El mensaje se retransmite a todos los suscriptores
    }

    @PostMapping("/send")
    public ResponseEntity<ChatMessage> sendMessage(@RequestBody ChatMessage message) {
        // Obtén el groupUser con user_id y group_id
        Groupuser groupUser = groupUserRepository.findByUserIdAndGroupId(message.getUserId(), message.getGroupId());

        if (groupUser == null) {
            return ResponseEntity.badRequest().body(null);
        }

        // Verifica que el grupo exista
        Groupname group = groupNameRepository.findById(message.getGroupId()).orElse(null);
        if (group == null) {
            return ResponseEntity.badRequest().body(null); // No existe el grupo
        }

        // Guarda el mensaje en la tabla groupMessenger
        GroupMessenger groupMessage = new GroupMessenger();
        groupMessage.setMessage(message.getContent());
        groupMessage.setGrupouser(groupUser); // Asigna la instancia completa
        groupMessage.setCreatedAt(LocalDate.now());
        groupMessengerRepository.save(groupMessage);

        // Retorna el mensaje para confirmar
        message.setTimestamp(System.currentTimeMillis());
        return ResponseEntity.ok(message);
    }


    // Crear un nuevo grupo
    @PostMapping("/create")
    public ResponseEntity<Groupname> createGroup(@RequestBody Groupname newGroup) {
        System.out.println("Recibido: " + newGroup);  // Verifica que se recibe el JSON correctamente

        // Revisa si el grupo existe
        boolean exists = groupNameRepository.existsByName(newGroup.getName());
        System.out.println("¿Ya existe el grupo? " + exists); // Log para ver el resultado

        if (exists) {
            return ResponseEntity.badRequest().body(null); // Ya existe un grupo con el mismo nombre
        }

        try {
            Groupname savedGroup = groupNameRepository.save(newGroup);
            System.out.println("Grupo guardado: " + savedGroup);
            return ResponseEntity.ok(savedGroup); // Retorna el grupo guardado
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Error en la base de datos o persistencia
        }
    }


    @PostMapping("/addUsersToGroup")
    public ResponseEntity<String> addUsersToGroup(
            @RequestParam Long groupId,
            @RequestParam Long userId1,
            @RequestParam Long userId2) {

        // Verificar si el grupo existe
        if (!groupNameRepository.existsById(groupId)) {
            return ResponseEntity.badRequest().body("El grupo no existe");
        }

        // Verificar si los usuarios ya están en el grupo
        if (groupUserRepository.existsByUserIdAndGroupId(userId1, groupId) ||
                groupUserRepository.existsByUserIdAndGroupId(userId2, groupId)) {
            return ResponseEntity.badRequest().body("Uno o ambos usuarios ya están en el grupo");
        }

        // Añadir ambos usuarios al grupo
        Groupuser user1 = new Groupuser(userId1, groupId);
        Groupuser user2 = new Groupuser(userId2, groupId);

        groupUserRepository.save(user1);
        groupUserRepository.save(user2);

        return ResponseEntity.ok("Usuarios añadidos al grupo exitosamente");
    }


}