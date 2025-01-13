package com.actividad_10.powerzone_back.Controllers;


import com.actividad_10.powerzone_back.Services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {

    private PostService postService;

   @PostMapping("/create")
    ResponseEntity<Void> createPost(@RequestParam int idUser) {
        postService.createPost(idUser);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    ResponseEntity<Void> deletePost(@RequestParam int idPost) {
        postService.deletePost(idPost);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get")
    ResponseEntity<Void> getPostByName(@RequestParam String name) {
        postService.findByTitle(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
