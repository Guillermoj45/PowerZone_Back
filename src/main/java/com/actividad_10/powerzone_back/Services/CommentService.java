package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.Config.JwtService;
import com.actividad_10.powerzone_back.Entities.Comment;
import com.actividad_10.powerzone_back.Entities.User;
import com.actividad_10.powerzone_back.Repositories.CommentRepository;
import com.actividad_10.powerzone_back.Repositories.UserRepository;
import io.jsonwebtoken.Claims;

import java.time.LocalDate;
import java.util.List;

public class CommentService implements ICommentService {

    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private final JwtService jwtService;

    public CommentService(JwtService jwtService) {
        this.jwtService = jwtService;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createComment(String token, Comment newComment) {
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);

        // Obtener el ID del usuario a partir del email
        Long userId = extractUserIdFromEmail(email);




        // Guardar el nuevo comentario
        commentRepository.save(newComment);
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
        List<Comment> hola = commentRepository.findByUser(userId);

    }
}
