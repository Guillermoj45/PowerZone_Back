package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.DTOs.PostDto;
import com.actividad_10.powerzone_back.Entities.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IPostService {
    PostDto createPost(String token, Post newPost, MultipartFile image);
    void deletePost(String token, Post deletePost);

    List<Post> findbestPost();
    void savePost(String token, Post post);
    void unsavePost(String token, Post post);
}
