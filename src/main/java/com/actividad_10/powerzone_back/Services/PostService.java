package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.Config.JwtService;
import com.actividad_10.powerzone_back.Entities.Booksmarks;
import com.actividad_10.powerzone_back.Entities.Post;
import com.actividad_10.powerzone_back.Entities.User;
import com.actividad_10.powerzone_back.Repositories.BooksmarksRepository;
import com.actividad_10.powerzone_back.Repositories.PostRepository;
import com.actividad_10.powerzone_back.Repositories.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostService implements IPostService {

    private PostRepository postRepository;

    private UserRepository userRepository;

    private final JwtService jwtService;

    private BooksmarksRepository booksmarksRepository;


    @Override
    public Post createPost(String token, Post newPost) {
        // Extraer datos del usuario desde el token
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);

        // Obtener el ID del usuario a partir del email
        User user = extractUserIdFromEmail(email);

        // Asignar el ID del usuario y la fecha de creación al nuevo post
        newPost.setUser(user);
        newPost.setCreatedAt(LocalDateTime.now());

        // Guardar el nuevo post
        postRepository.save(newPost);
        return newPost;
    }
    
    private User extractUserIdFromEmail(String email) {
        return userRepository.findByEmail(email).get();
    }
    @Transactional
    @Override
    public void deletePost(String token, Post deletePost) {
        // Extraer datos del usuario desde el token
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);
        String rol = claims.get("rol", String.class);

        // Obtener el ID del usuario a partir del email
        User user = extractUserIdFromEmail(email);

        // Verificar que el post pertenece al usuario
        if (deletePost.getUser().getId().equals(user.getId())) {
            postRepository.deleteById(deletePost.getId());
        } else if(rol.equals("ADMIN")) {
            deletePost.setDelete(true);
            postRepository.save(deletePost);
        }else  {
                throw new RuntimeException("No tienes permiso para eliminar este post");
            }

    }

    public void findallPost(Post userPosts) {
        postRepository.findAll();
    }

    public void safePost(Post post) {

    }

    public void likePost(String token, Long postId) {
    }

    public void sharePost(String token, Long postId) {
    }
}
