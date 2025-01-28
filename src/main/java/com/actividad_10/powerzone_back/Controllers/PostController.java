package com.actividad_10.powerzone_back.Controllers;


import com.actividad_10.powerzone_back.Config.JwtService;
import com.actividad_10.powerzone_back.DTOs.PostDto;
import com.actividad_10.powerzone_back.DTOs.CreateReportDTO;
import com.actividad_10.powerzone_back.DTOs.TokenDto;
import com.actividad_10.powerzone_back.Entities.Post;
import com.actividad_10.powerzone_back.Services.PostService;
import com.actividad_10.powerzone_back.Services.ReportService;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post")
@AllArgsConstructor
public class PostController {

    private PostService postService;
    private ReportService reportService;
    private JwtService jwtService;


    @PostMapping("/create")
    ResponseEntity<PostDto> createPost(@RequestHeader("Authorization") String token, @RequestBody Post post) {
        PostDto createdPost = postService.createPost(token, post);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @DeleteMapping("/delete")
    ResponseEntity<Void> deletePost(@RequestHeader("Authorization") String token, @RequestBody Post deletePost) {
        postService.deletePost(token,deletePost);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/getbest")
    ResponseEntity<List<Post>> getbestPost(@RequestHeader("Authorization") String token) {
        List<Post> bestPosts = postService.findbestPost();
        return ResponseEntity.status(HttpStatus.OK).body(bestPosts);
    }

    @GetMapping("/all")
    ResponseEntity<List<PostDto>> getAllPosts() {
        List<PostDto> allPosts = postService.getAllPosts();
        return ResponseEntity.status(HttpStatus.OK).body(allPosts);
    }
    @GetMapping("/userposts")
    ResponseEntity<List<Post>> getUserPosts(@RequestHeader("Authorization") String token) {
        List<Post> userPosts = postService.finduserPost(token);
        return ResponseEntity.status(HttpStatus.OK).body(userPosts);
    }
    @PostMapping("/save")
    ResponseEntity<String> savePost(@RequestHeader("Authorization") String token, @RequestBody Post post) {
        postService.savePost(token,post);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post save");
    }
    @PostMapping("/unsave")
    ResponseEntity<String> unsavePost(@RequestHeader("Authorization") String token, @RequestBody Post post) {
        postService.unsavePost(token,post);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post unsave");
    }

    @PostMapping("/like")
    ResponseEntity<String> likePost(@RequestHeader("Authorization") String token, @RequestBody Post post) {
        postService.likePost(token, post);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post liked");
    }
    @PostMapping("/unlike")
    ResponseEntity<String> unlikePost(@RequestHeader("Authorization") String token, @RequestBody Post post) {
        postService.unlikePost(token, post);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post unliked");
    }


    @PostMapping("/share")
    ResponseEntity<String> sharePost(@RequestHeader("Authorization") String token, @RequestBody Map<String, Long> share) {
        postService.sharePost(token, share.get("postId"));
        return ResponseEntity.status(HttpStatus.CREATED).body("Post shared");
    }

    @PostMapping("/report")
    public ResponseEntity<String> reportPost(@RequestBody CreateReportDTO report, Principal principal) {
        reportService.reportPost(report, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body("Post reported");
    }


}
