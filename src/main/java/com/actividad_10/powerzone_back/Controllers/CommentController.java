package com.actividad_10.powerzone_back.Controllers;


import com.actividad_10.powerzone_back.Entities.Comment;
import com.actividad_10.powerzone_back.Services.CommentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
public class CommentController {
    private CommentService commentService;

    @PostMapping("/create")
    void createComment(@RequestBody Comment newComment) {
        commentService.createComment(newComment);
    }
    @PostMapping("/delete")
    void deleteComment(@RequestBody Comment deleteComment) {
        commentService.deleteComment(deleteComment.getId());
    }
    @PostMapping("/getuser")
    void getCommentByUserName(@RequestBody Comment userComments) {
        commentService.getCommentByUserName(userComments);
    }
}
