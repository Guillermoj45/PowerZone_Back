package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.Config.JwtService;
import com.actividad_10.powerzone_back.Entities.Comment;
import com.actividad_10.powerzone_back.Entities.Post;
import com.actividad_10.powerzone_back.Entities.User;
import com.actividad_10.powerzone_back.Repositories.CommentRepository;
import com.actividad_10.powerzone_back.Repositories.PostRepository;
import com.actividad_10.powerzone_back.Repositories.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PostRepository postRepository;

    public CommentService(JwtService jwtService, CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.jwtService = jwtService;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Comment createComment(String token, Comment newComment) {
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);

        // Obtener el ID del usuario a partir del email
        Long userId = extractUserIdFromEmail(email);

        // Asignar el ID del usuario al comentario
        newComment.setUser(userRepository.findById(userId).get());

        // Verificar que el objeto Post no sea nulo
        if (newComment.getPost() == null || newComment.getPost().getId() == null) {
            throw new RuntimeException("Post ID is required");
        }

        // Obtener el Post usando el post_id
        Optional<Post> post = postRepository.findById(newComment.getPost().getId());
        if (post.isPresent()) {
            Post existingPost = post.get();
            newComment.setPost(existingPost);
            // Asignar la fecha de creación del Post al comentario
            newComment.setPostCreatedAt(existingPost.getCreatedAt());
        } else {
            throw new RuntimeException("Post not found");
        }

        newComment.setCreatedAt(LocalDateTime.now());
        // Guardar el nuevo comentario
        commentRepository.save(newComment);
        return newComment;
    }
    private Long extractUserIdFromEmail(String email) {
        return userRepository.findByEmail(email).get().getId();
    }
    @Override
    public void deleteComment(Long idComment) {
        commentRepository.deleteById(idComment);

    }

    @Override
    public void getCommentByUserName(Comment userComments) {
        User userId = userComments.getUser();
        commentRepository.findByUser(userId);
    }
}
