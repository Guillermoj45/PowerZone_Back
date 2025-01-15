package com.actividad_10.powerzone_back.Controllers;


import com.actividad_10.powerzone_back.Entities.Post;
import com.actividad_10.powerzone_back.Services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {

    private PostService postService;

   @PostMapping("/create")
    ResponseEntity<Void> createPost(@RequestBody Post newPost) {
        postService.createPost(newPost);
        return new ResponseEntity<>(HttpStatus.CREATED);
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
