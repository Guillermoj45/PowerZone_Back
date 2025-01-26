package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.Entities.Post;

import java.util.List;

public interface IPostService {
    Post createPost(String token, Post newPost);
    void deletePost(String token, Post deletePost);

    List<Post> findbestPost();
    void savePost(String token, Post post);
    void unsavePost(String token, Post post);
}
