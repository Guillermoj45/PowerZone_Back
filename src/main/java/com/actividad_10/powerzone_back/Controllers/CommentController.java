package com.actividad_10.powerzone_back.Controllers;


import com.actividad_10.powerzone_back.Entities.Comment;
import com.actividad_10.powerzone_back.Services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    @PostMapping("/create")
    public Comment createComment(@RequestHeader("Authorization") String token, @RequestBody Comment comment) {
        return commentService.createComment(token, comment);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteComment(@RequestHeader("Authorization") String token, @RequestBody Comment deleteComment) {
        commentService.deleteComment(token, deleteComment);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/getuser")
    void getCommentByUserName(@RequestBody Comment userComments) {
        commentService.getCommentByUserName(userComments);
    }
}
