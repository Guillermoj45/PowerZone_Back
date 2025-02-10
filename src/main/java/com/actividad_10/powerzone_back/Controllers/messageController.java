package com.actividad_10.powerzone_back.Controllers;

import com.actividad_10.powerzone_back.DTOs.ChatMessage;
import com.actividad_10.powerzone_back.Entities.GroupMessenger;
import com.actividad_10.powerzone_back.Entities.GroupName;
import com.actividad_10.powerzone_back.Entities.GroupUser;
import com.actividad_10.powerzone_back.Repositories.GroupMessengerRepository;
import com.actividad_10.powerzone_back.Repositories.GroupNameRepository;
import com.actividad_10.powerzone_back.Repositories.GroupUserRepository;
import com.actividad_10.powerzone_back.Services.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@AllArgsConstructor
public class messageController {

    private final GroupMessengerRepository groupMessengerRepository;
    private final GroupUserRepository groupUserRepository;
    private final GroupNameRepository groupNameRepository;
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;


    // Manejar mensajes enviados por los clientes
    @MessageMapping("/chat/{roomId}") // Los clientes envían mensajes a /app/chat
    @SendTo("/topic/messages/{roomId}") // Los mensajes se envían a los suscriptores de /topic/messages
    public ChatMessage send(@DestinationVariable String roomId, ChatMessage message) {
        // Aquí puedes guardar el mensaje en la base de datos si es necesario
        // message.setTimestamp(System.currentTimeMillis());
        messageService.saveMessage(message, "");

        System.out.println("Mensaje recibido en el servidor: " + message);
        return message; // El mensaje se retransmite a todos los suscriptores
    }

    //TODO: Implementar el método para guardar el mensaje en la base de datos
    @GetMapping("/try")
    public void tryMessage() {
        ChatMessage message = new ChatMessage();
        message.setUserId(1L);
        message.setGroupId(1L);
        message.setContent("Hola");
        message.setTimestamp(String.valueOf(System.currentTimeMillis()));
        messagingTemplate.convertAndSend("/topic/messages/1", message);
        System.out.println("Mensaje enviado");
    }

    @PostMapping("/send")
    public ResponseEntity<ChatMessage> sendMessage(@RequestBody ChatMessage message) {
        // Obtén el groupUser con user_id y group_id
        GroupUser groupUser = groupUserRepository.findByUserIdAndGroupId(message.getUserId(), message.getGroupId());

        if (groupUser == null) {
            return ResponseEntity.badRequest().body(null);
        }

        // Verifica que el grupo exista
        GroupName group = groupNameRepository.findById(message.getGroupId()).orElse(null);
        if (group == null) {
            return ResponseEntity.badRequest().body(null); // No existe el grupo
        }

        // Guarda el mensaje en la tabla groupMessenger
        GroupMessenger groupMessage = new GroupMessenger();
        groupMessage.setMessage(message.getContent());
        groupMessage.setGrupouser(groupUser); // Asigna la instancia completa
        groupMessage.setCreatedAt(LocalDateTime.now());
        groupMessengerRepository.save(groupMessage);

        // Retorna el mensaje para confirmar
        message.setTimestamp(String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok(message);
    }


    // Crear un nuevo grupo
    @PostMapping("/create")
    public ResponseEntity<GroupName> createGroup(@RequestBody GroupName newGroup) {
        System.out.println("Recibido: " + newGroup);  // Verifica que se recibe el JSON correctamente

        // Revisa si el grupo existe
        boolean exists = groupNameRepository.existsByName(newGroup.getName());
        System.out.println("¿Ya existe el grupo? " + exists); // Log para ver el resultado

        if (exists) {
            return ResponseEntity.badRequest().body(null); // Ya existe un grupo con el mismo nombre
        }

        try {
            GroupName savedGroup = groupNameRepository.save(newGroup);
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
        GroupUser user1 = new GroupUser(userId1, groupId);
        GroupUser user2 = new GroupUser(userId2, groupId);

        groupUserRepository.save(user1);
        groupUserRepository.save(user2);

        return ResponseEntity.ok("Usuarios añadidos al grupo exitosamente");
    }

    @GetMapping("/checkGroup")
    public ResponseEntity<GroupName> checkGroup(
            @RequestParam Long userId1,
            @RequestParam Long userId2) {
        // Busca todos los grupos donde pertenece el primer usuario
        List<Long> user1GroupIds = groupUserRepository.findGroupIdsByUserId(userId1);
        // Busca todos los grupos donde pertenece el segundo usuario
        List<Long> user2GroupIds = groupUserRepository.findGroupIdsByUserId(userId2);

        // Encuentra un grupo común
        for (Long groupId : user1GroupIds) {
            if (user2GroupIds.contains(groupId)) {
                // Obtén el grupo común
                GroupName commonGroup = groupNameRepository.findById(groupId).orElse(null);
                if (commonGroup != null) {
                    return ResponseEntity.ok(commonGroup);
                }
            }
        }
        return ResponseEntity.ok(null); // No hay grupo común
    }
}