package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.Config.JwtService;
import com.actividad_10.powerzone_back.DTOs.CommentDetailsDto;
import com.actividad_10.powerzone_back.DTOs.CommentDto;
import com.actividad_10.powerzone_back.Entities.Comment;
import com.actividad_10.powerzone_back.Entities.Post;
import com.actividad_10.powerzone_back.Entities.User;
import com.actividad_10.powerzone_back.Repositories.CommentRepository;
import com.actividad_10.powerzone_back.Repositories.PostRepository;
import com.actividad_10.powerzone_back.Repositories.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class
CommentService implements ICommentService {

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
    public CommentDto createComment(String token, Comment newComment) {
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);
        Long userId = extractUserIdFromEmail(email);

        newComment.setUser(userRepository.findById(userId).get());

        if (newComment.getPost() == null || newComment.getPost().getId() == null) {
            throw new RuntimeException("Post ID is required");
        }

        Optional<Post> postOptional = postRepository.findById(newComment.getPost().getId());
        if (!postOptional.isPresent()) {
            throw new RuntimeException("Post not found");
        }

        Post post = postOptional.get();
        newComment.setPost(post);
        newComment.setPostCreatedAt(post.getCreatedAt());
        newComment.setCreatedAt(LocalDateTime.now());


        commentRepository.save(newComment);

        CommentDto commentDto = new CommentDto();
        commentDto.setId(newComment.getId());
        commentDto.setCreatedAt(newComment.getCreatedAt());
        commentDto.setContent(newComment.getContent());
        commentDto.setPostId(newComment.getPost().getId());
        commentDto.setUserId(newComment.getUser().getId());

        return commentDto;
    }

    CommentDto getComentarioById(Long id){
        Comment comment = commentRepository.findById(id).get();
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setCreatedAt(comment.getCreatedAt());
        commentDto.setContent(comment.getContent());
        commentDto.setPostId(comment.getPost().getId());
        commentDto.setUserId(comment.getUser().getId());
        return commentDto;
    }

    private Long extractUserIdFromEmail(String email) {
        return userRepository.findByEmail(email).get().getId();
    }
    @Override
    @Transactional
    public void deleteComment(String token, Comment deleteComment) {
        // Extraer datos del usuario desde el token
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);
        String rol = claims.get("rol", String.class);

        // Obtener el ID del usuario a partir del email
        Long userId = extractUserIdFromEmail(email);

        // Verificar que el comentario pertenece al usuario
        if (deleteComment.getUser().getId().equals(userId)) {
            commentRepository.deleteById(deleteComment.getId());
        } else if (rol.equals("ADMIN")) {
            deleteComment.setDelete(true);
            commentRepository.save(deleteComment);
        } else {
            throw new RuntimeException("No tienes permiso para eliminar este comentario");
        }
    }
    public List<CommentDetailsDto> getAllCommentsByPostId(Long postId) {
        return commentRepository.findAllCommentDetailsByPostId(postId);
    }
    @Override
    public void getCommentByUserName(Comment userComments) {
        User userId = userComments.getUser();
        commentRepository.findByUser(userId);
    }
}
