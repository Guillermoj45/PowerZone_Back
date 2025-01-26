package com.actividad_10.powerzone_back.Controllers;


import com.actividad_10.powerzone_back.Config.JwtService;
import com.actividad_10.powerzone_back.Entities.Post;
import com.actividad_10.powerzone_back.Services.PostService;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create")
    ResponseEntity<Post> createPost(@RequestHeader("Authorization") String token, @RequestBody Post post) {
        Post createdPost = postService.createPost(token, post);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @DeleteMapping("/delete")
    ResponseEntity<Void> deletePost(@RequestHeader("Authorization") String token, @RequestBody Post deletePost) {
        postService.deletePost(token,deletePost);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/getbest")
    ResponseEntity<Void> getbestPost() {
        postService.findbestPost();
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/userposts")
    ResponseEntity<List<Post>> getUserPosts(@RequestHeader("Authorization") String token) {
        List<Post> userPosts = postService.finduserPost(token);
        return ResponseEntity.status(HttpStatus.OK).body(userPosts);
    }
    @PostMapping("/save")
    ResponseEntity<Void> savePost(@RequestHeader("Authorization") String token,@RequestBody Post post) {
        postService.savePost(token,post);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/unsave")
    ResponseEntity<Void> unsavePost(@RequestHeader("Authorization") String token,@RequestBody Post post) {
        postService.unsavePost(token,post);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/like")
    ResponseEntity<String> likePost(@RequestHeader("Authorization") String token, @RequestBody Post post) {
        postService.likePost(token, post);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post liked");
    }
    @PostMapping("/unlike")
    ResponseEntity<String> unlikePost(@RequestHeader("Authorization") String token, @RequestBody Post post) {
        postService.unlikePost(token, post);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post liked");
    }


    @PostMapping("/share")
    ResponseEntity<String> sharePost(@RequestHeader("Authorization") String token, @RequestBody Map<String, Long> share) {
        postService.sharePost(token, share.get("postId"));
        return ResponseEntity.status(HttpStatus.CREATED).body("Post shared");
    }



}
