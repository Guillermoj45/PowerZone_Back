package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.Config.JwtService;
import com.actividad_10.powerzone_back.Entities.Post;
import com.actividad_10.powerzone_back.Entities.User;
import com.actividad_10.powerzone_back.Repositories.PostRepository;
import com.actividad_10.powerzone_back.Repositories.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class PostService implements IPostService {
    @Autowired
    private PostRepository postRepository;

    private UserRepository userRepository;

    private final JwtService jwtService;

    public PostService(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    public void createPost(String token, Post newPost) {
        // Extraer datos del usuario desde el token
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);

        // Obtener el ID del usuario a partir del email
        Long userId = extractUserIdFromEmail(email);

        // Asignar el ID del usuario y la fecha de creación al nuevo post
        newPost.setUserId(userId);
        newPost.setCreatedAt(java.time.LocalDate.now());

        // Guardar el nuevo post
        postRepository.save(newPost);
    }
    
    private Long extractUserIdFromEmail(String email) {
        return userRepository.findByEmail(email).get().getId();
    }
    @Override
    public void deletePost(Long idPost) {
        postRepository.deleteById(idPost);
    }

    public void findallPost(Post userPosts) {
        postRepository.findAll();
    }
    @Override
    public Optional<Post> findByTitle(String namePost) {
        return postRepository.findByTitle(namePost);
    }

}
