package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.Config.JwtService;
import com.actividad_10.powerzone_back.Entities.Booksmarks;
import com.actividad_10.powerzone_back.Entities.LikePost;
import com.actividad_10.powerzone_back.Entities.Post;
import com.actividad_10.powerzone_back.Repositories.BooksmarksRepository;
import com.actividad_10.powerzone_back.Repositories.LikePostRepository;
import com.actividad_10.powerzone_back.Repositories.PostRepository;
import com.actividad_10.powerzone_back.Repositories.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService implements IPostService {
    @Autowired
    private PostRepository postRepository;

    private UserRepository userRepository;

    private final JwtService jwtService;

    private BooksmarksRepository booksmarksRepository;

    private LikePostRepository likePostRepository;

    public PostService(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;

    }

    @Override
    public Post createPost(String token, Post newPost) {
        // Extraer datos del usuario desde el token
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);

        // Obtener el ID del usuario a partir del email
        Long userId = extractUserIdFromEmail(email);

        // Asignar el ID del usuario y la fecha de creación al nuevo post
        newPost.setUserId(userId);
        newPost.setCreatedAt(LocalDateTime.now());

        // Guardar el nuevo post
        postRepository.save(newPost);
        return newPost;
    }
    
    private Long extractUserIdFromEmail(String email) {
        return userRepository.findByEmail(email).get().getId();
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
        Long userId = extractUserIdFromEmail(email);

        // Verificar que el post pertenece al usuario
        if (deletePost.getUserId().equals(userId)) {
            postRepository.deleteById(deletePost.getId());
        } else if(rol.equals("ADMIN")) {
            deletePost.setDelete(true);
            postRepository.save(deletePost);
        }else  {
                throw new RuntimeException("No tienes permiso para eliminar este post");
            }

    }

    public List<Post> findbestPost() {
        return postRepository.findPostsWithMostLikes();

    }

    public List<Post> finduserPost(String token) {
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);


        // Obtener el ID del usuario a partir del email
        Long userId = extractUserIdFromEmail(email);
        return postRepository.findAllByUserId(userId);
    }

    public void savePost(String token, Post post) {
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);

        Long userId = extractUserIdFromEmail(email);
        Booksmarks booksmarks = new Booksmarks();
        booksmarks.setUserId(userId);
        booksmarks.setPostId(post.getId());
        booksmarks.setCreatedAtPost(postRepository.findCreatedAtById(post.getId()).get());
        booksmarksRepository.save(booksmarks);

    }

    @Override
    public void unsavePost(String token, Post post) {
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);

        Long userId = extractUserIdFromEmail(email);
        booksmarksRepository.deleteById(userId, post.getId());
    }

    public void likePost(String token, Post post) {
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);

        Long userId = extractUserIdFromEmail(email);
        LikePost likePost = new LikePost();
        likePost.setUserId(userId);
        likePost.setPostId(post.getId());
        likePost.setCreatedAtPost(postRepository.findCreatedAtById(post.getId()).get());
        likePostRepository.save(likePost);

    }
    public void unlikePost(String token, Post post) {
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);

        Long userId = extractUserIdFromEmail(email);
        likePostRepository.deleteById(userId, post.getId());

    }

    public void sharePost(String token, Long postId) {
    }


}
