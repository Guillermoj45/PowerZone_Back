package com.actividad_10.powerzone_back.Controllers;

import com.actividad_10.powerzone_back.DTOs.CommentDetailsDto;
import com.actividad_10.powerzone_back.DTOs.PostDto;
import com.actividad_10.powerzone_back.Entities.Post;
import com.actividad_10.powerzone_back.Repositories.PostRepository;
import com.actividad_10.powerzone_back.Services.CommentService;
import com.actividad_10.powerzone_back.Services.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private final PostService postService;
    private final ObjectMapper objectMapper; // Para convertir JSON a objeto Java

    @Autowired
    private CommentService commentService;

    public PostController(PostService postService, ObjectMapper objectMapper) {
        this.postService = postService;
        this.objectMapper = objectMapper;
    }

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
    @GetMapping("/getbest")
    ResponseEntity<List<Post>> getbestPost(@RequestHeader("Authorization") String token) {
        List<Post> bestPosts = postService.findbestPost();
        return ResponseEntity.status(HttpStatus.OK).body(bestPosts);
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
    @GetMapping("/userposts")
    ResponseEntity<List<Post>> getUserPosts(@RequestHeader("Authorization") String token) {
        List<Post> userPosts = postService.finduserPost(token);
        return ResponseEntity.status(HttpStatus.OK).body(userPosts);
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
        PostDto postDto = postService.getPostById(token, postId);
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

    @GetMapping("/userposts/{userId}")
    public ResponseEntity<List<Post>> getPostsByUserId(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String token) {
        List<Post> posts = postService.getPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }



}
