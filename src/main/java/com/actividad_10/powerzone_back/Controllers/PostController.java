package com.actividad_10.powerzone_back.Controllers;


import com.actividad_10.powerzone_back.Config.JwtService;
import com.actividad_10.powerzone_back.Entities.Post;
import com.actividad_10.powerzone_back.Services.PostService;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/post")
public class PostController {

    private PostService postService;
    private final JwtService jwtService;

    public PostController(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    @PostMapping("/create")
    public ResponseEntity<String> createPost(
            @RequestHeader("Authorization") String token, // JWT desde el encabezado
            @RequestBody Post post                        // JSON del cuerpo
    ) {
        // Extraer datos del usuario desde el token
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("tokenDto", Map.class).get("email").toString();

        // Asociar el usuario al post
        post.setUserId(extractUserIdFromEmail(email));

        // Guardar el post en la base de datos
        postService.createPost(post);

        return ResponseEntity.status(HttpStatus.CREATED).body("Post creado exitosamente");
    }
    private Long extractUserIdFromEmail(String email) {
        return postService.extractUserPosts(email);
    }
    @DeleteMapping("/delete")
    ResponseEntity<Void> deletePost(@RequestBody Post deletePost) {
        postService.deletePost(deletePost.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/getall")
    ResponseEntity<Void> getallPost(@RequestBody Post userPosts) {
        postService.findallPost(userPosts);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/getitle")
    ResponseEntity<Void> getPostByName(@RequestParam String name) {
        postService.findByTitle(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
