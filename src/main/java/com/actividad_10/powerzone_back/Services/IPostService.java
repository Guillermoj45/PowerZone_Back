package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.Entities.Post;
import jakarta.transaction.Transactional;

import java.util.Optional;

public interface IPostService {
    Post createPost(String token, Post newPost);
    void deletePost(String token, Post deletePost);
    void findallPost(Post userPosts);

}
