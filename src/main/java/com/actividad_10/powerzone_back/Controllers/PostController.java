package com.actividad_10.powerzone_back.Controllers;

import com.actividad_10.powerzone_back.Config.JwtService;
import com.actividad_10.powerzone_back.DTOs.CommentDetailsDto;
import com.actividad_10.powerzone_back.DTOs.CreateReportDTO;
import com.actividad_10.powerzone_back.DTOs.PostDto;
import com.actividad_10.powerzone_back.Entities.Post;
import com.actividad_10.powerzone_back.Repositories.PostRepository;
import com.actividad_10.powerzone_back.Services.CommentService;
import com.actividad_10.powerzone_back.Services.PostService;
import com.actividad_10.powerzone_back.Services.ReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post")
@AllArgsConstructor
public class PostController {
    private final PostRepository postRepository;
    private final PostService postService;
    private final ObjectMapper objectMapper; // Para convertir JSON a objeto Java
    private final CommentService commentService;
    private final ReportService reportService;
    private final JwtService jwtService;


    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    public ResponseEntity<PostDto> createPost(
            @RequestHeader("Authorization") String token,
            @RequestPart("post") String postJson,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        System.out.println("Token recibido: " + token);
        System.out.println("Post recibido en JSON: " + postJson);

        // Convertir JSON a objeto Post
        Post post;
        try {
            post = objectMapper.readValue(postJson, Post.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Llamar al servicio
        PostDto createdPost = postService.createPost(token, post, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @DeleteMapping("/delete")
    ResponseEntity<Void> deletePost(@RequestHeader("Authorization") String token, @RequestBody Post deletePost) {
        postService.deletePost(token,deletePost);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/followed")
    public ResponseEntity<List<PostDto>> getPostsFromFollowedUsers(@RequestHeader("Authorization") String token) {
        List<PostDto> followedUserPosts = postService.getPostsFromFollowedUsers(token);
        return ResponseEntity.status(HttpStatus.OK).body(followedUserPosts);
    }

    @GetMapping("/all")
    ResponseEntity<List<PostDto>> getAllPosts(@RequestHeader("Authorization") String token) {
        List<PostDto> allPosts = postService.getAllPosts();
        return ResponseEntity.status(HttpStatus.OK).body(allPosts);
    }

    @GetMapping("/hasLiked")
    public ResponseEntity<Boolean> hasUserLikedPost(
            @RequestHeader("Authorization") String token,
            @RequestParam Long postId) {
        boolean hasLiked = postService.hasUserLikedPost(token, postId);
        return ResponseEntity.status(HttpStatus.OK).body(hasLiked);
    }


    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> savePost(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Long> postIdMap) {
        System.out.println("Token recibido: " + token);
        Long postId = postIdMap.get("postId");
        System.out.println("Post ID recibido: " + postId);

        if (postId == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Post ID is required"));
        }
        Post post = postService.findaById(postId);
        postService.savePost(token, post);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("message", "Post saved"));
    }

    @PostMapping("/unsave")
    public ResponseEntity<Map<String, String>> unsavePost(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Long> postIdMap) {
        System.out.println("Token recibido: " + token);
        Long postId = postIdMap.get("postId");
        if (postId == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Post ID is required"));
        }
        Post post = postService.findaById(postId);
        postService.unsavePost(token, post);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of("message", "Post unsaved"));
    }


    @PostMapping("/like")
    public ResponseEntity<Map<String, String>> likePost(
            @RequestHeader("Authorization") String token,
            @RequestBody Long postId) {

        Post post = postService.findaById(postId);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Post not found"));
        }

        postService.likePost(token, post);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Post liked"));
    }

    @PostMapping("/unlike")
    public ResponseEntity<Map<String, String>> unlikePost(
            @RequestHeader("Authorization") String token,
            @RequestBody Long postId) {

        Post post = postService.findaById(postId);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Post not found"));
        }

        postService.unlikePost(token, post);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Post unliked"));
    }


    @PostMapping("/hasLiked")
    public ResponseEntity<Boolean> hasLikedPost(
            @RequestHeader("Authorization") String token,
            @RequestBody Long postId) {
        boolean hasLiked = postService.hasUserLikedPost(token, postId);
        return ResponseEntity.ok(hasLiked);
    }

    @PostMapping("/hasSaved")
    public ResponseEntity<Boolean> hasSavedPost(
            @RequestHeader("Authorization") String token,
            @RequestBody Long postId) {
        boolean hasSaved = postService.hasUserSavedPost(token, postId);
        return ResponseEntity.ok(hasSaved);
    }

    @GetMapping("/user/saved")
    public ResponseEntity<List<PostDto>> getAllSavedPosts(@RequestHeader("Authorization") String token) {
        List<PostDto> savedPosts = postService.getAllSavedPosts(token);
        return ResponseEntity.status(HttpStatus.OK).body(savedPosts);
    }

    @PostMapping("/share")
    ResponseEntity<String> sharePost(@RequestHeader("Authorization") String token, @RequestBody Map<String, Long> share) {
        postService.sharePost(token, share.get("postId"));
        return ResponseEntity.status(HttpStatus.CREATED).body("Post shared");
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPostById(
            @RequestHeader("Authorization") String token,
            @PathVariable Long postId) {
        PostDto postDto = postService.getPostById(postId);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDetailsDto>> getAllCommentsByPostId(
            @RequestHeader("Authorization") String token,
            @PathVariable Long postId) {
        List<CommentDetailsDto> comments = commentService.getAllCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/user/{userId}")
    public List<Post> getPostsByUser(@PathVariable Long userId) {
        return postRepository.findByUserId(userId);
    }

    @GetMapping("/userposts")
    public ResponseEntity<List<PostDto>> getUserPosts(@RequestHeader("Authorization") String token) {
        List<PostDto> userPosts = postService.getUserPosts(token);
        return ResponseEntity.status(HttpStatus.OK).body(userPosts);
    }

    @GetMapping("/userposts/{userId}")
    public ResponseEntity<List<PostDto>> getPostsByUserId(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String token) {
        List<PostDto> posts = postService.getPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/report")
    public ResponseEntity<String> reportPost(
            @RequestHeader("Authorization") String token,
            @RequestBody CreateReportDTO report) {

        String jwt = token.replace("Bearer ", "");
        String email = jwtService.extractTokenData(jwt).getEmail();

        reportService.reportPost(report,email);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post reported");
    }


}
