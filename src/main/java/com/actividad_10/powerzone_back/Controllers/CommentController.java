package com.actividad_10.powerzone_back.Controllers;


import com.actividad_10.powerzone_back.DTOs.CommentDto;
import com.actividad_10.powerzone_back.DTOs.CommentRequest;
import com.actividad_10.powerzone_back.Entities.Comment;
import com.actividad_10.powerzone_back.Entities.Post;
import com.actividad_10.powerzone_back.Repositories.PostRepository;
import com.actividad_10.powerzone_back.Services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    private final PostRepository postRepository;
    public CommentController(CommentService commentService, PostRepository postRepository) {
        this.commentService = commentService;
        this.postRepository = postRepository;
    }
    @PostMapping("/create")
    ResponseEntity<CommentDto> createComment(@RequestHeader("Authorization") String token,
                                             @RequestBody CommentRequest request) {
        System.out.println("Post ID recibido: " + request.getPostId());
        System.out.println("Contenido recibido: " + request.getContent());


        Optional<Post> postOptional = postRepository.findById(request.getPostId());
        if (!postOptional.isPresent()) {
            throw new RuntimeException("Post not found");
        }

        Post post = postOptional.get();
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setContent(request.getContent());

        CommentDto createdComment = commentService.createComment(token, comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }
    @DeleteMapping("/delete")
    ResponseEntity<Void> deleteComment(@RequestHeader("Authorization") String token, @RequestBody Comment deleteComment) {
        commentService.deleteComment(token, deleteComment);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/getuser")
    void getCommentByUserName(@RequestBody Comment userComments) {
        commentService.getCommentByUserName(userComments);
    }
}
