package com.actividad_10.powerzone_back.Controllers;

import com.actividad_10.powerzone_back.Config.JwtService;
import com.actividad_10.powerzone_back.DTOs.ChatMessage;
import com.actividad_10.powerzone_back.DTOs.TokenDto;
import com.actividad_10.powerzone_back.Entities.GroupMessenger;
import com.actividad_10.powerzone_back.Entities.GroupName;
import com.actividad_10.powerzone_back.Entities.GroupUser;
import com.actividad_10.powerzone_back.Entities.User;
import com.actividad_10.powerzone_back.Repositories.GroupMessengerRepository;
import com.actividad_10.powerzone_back.Repositories.GroupNameRepository;
import com.actividad_10.powerzone_back.Repositories.GroupUserRepository;
import com.actividad_10.powerzone_back.Repositories.UserRepository;
import com.actividad_10.powerzone_back.Services.MessageService;
import lombok.AllArgsConstructor;
import com.actividad_10.powerzone_back.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/messages")
@AllArgsConstructor
public class messageController {

    private final GroupMessengerRepository groupMessengerRepository;
    private final GroupUserRepository groupUserRepository;
    private final GroupNameRepository groupNameRepository;
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;
    private JwtService jwtService;

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
    public ResponseEntity<Map<String, String>> addUsersToGroup(
            @RequestParam Long groupId,
            @RequestBody List<Long> userIds) { // Recibe una lista de IDs

        // Verificar si el grupo existe
        if (!groupNameRepository.existsById(groupId)) {
            return ResponseEntity.badRequest().body(Map.of("message", "El grupo no existe"));
        }

        List<GroupUser> usersToAdd = new ArrayList<>();

        for (Long userId : userIds) {
            // Verificar si el usuario ya está en el grupo
            if (groupUserRepository.existsByUserIdAndGroupId(userId, groupId)) {
                return ResponseEntity.badRequest().body(Map.of("message", "El usuario con ID " + userId + " ya está en el grupo"));
            }
            usersToAdd.add(new GroupUser(userId, groupId));
        }
        GroupName groupName = groupNameRepository.getById(groupId);
        User user1 = userRepository.getById(userId1);
        User user2 = userRepository.getById(userId1);

        // Añadir ambos usuarios al grupo
        GroupUser groupUser1 = new GroupUser(user1, groupName);
        GroupUser groupUser2 = new GroupUser(user2, groupName);
        // Guardar los usuarios en el grupo
        groupUserRepository.saveAll(usersToAdd);

        return ResponseEntity.ok(Map.of("message", "Usuarios añadidos al grupo exitosamente"));
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

    @GetMapping("/info")
    public ResponseEntity<?> obtenerInfoUsuarioYGrupos(@RequestHeader("Authorization") String token) {
        try {
            // Elimina el prefijo "Bearer " del token
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // Extrae los datos del token
            TokenDto tokenData = jwtService.extractTokenData(token);

            // Obtén el email del token
            String email = tokenData.getEmail();

            // Busca al usuario por el email en la base de datos
            Optional<User> usuarioOpt = userRepository.findByEmail(email);

            // Verifica si el usuario existe
            if (usuarioOpt.isPresent()) {
                User usuario = usuarioOpt.get();
                Long userId = usuario.getId();

                // Obtén todos los grupos a los que pertenece el usuario
                List<GroupName> grupos = groupUserRepository.findAllByUserId(userId);

                // Devuelve la información del usuario y los grupos
                return ResponseEntity.ok().body(Map.of(
                        "id", usuario.getId(),
                        "email", usuario.getEmail(),
                        "rol", tokenData.getRol(),
                        "grupos", grupos,
                        "fecha_creacion", tokenData.getFecha_creacion(),
                        "fecha_expiracion", tokenData.getFecha_expiracion()
                ));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no autorizado.");
        }
    }



}