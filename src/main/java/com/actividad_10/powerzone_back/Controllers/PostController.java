package com.actividad_10.powerzone_back.Controllers;


import com.actividad_10.powerzone_back.Config.JwtService;
import com.actividad_10.powerzone_back.Entities.Post;
import com.actividad_10.powerzone_back.Services.PostService;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/post")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPost(@RequestHeader("Authorization") String token, @RequestBody Post post) {

        postService.createPost(token,post);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post creado exitosamente");
    }

    @DeleteMapping("/delete")
    ResponseEntity<Void> deletePost(@RequestHeader("Authorization") String token, @RequestBody Post deletePost) {
        postService.deletePost(token,deletePost);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/getall")
    ResponseEntity<Void> getallPost(@RequestBody Post userPosts) {
        postService.findallPost(userPosts);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
