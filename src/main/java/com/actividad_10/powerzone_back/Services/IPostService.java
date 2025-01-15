package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.Entities.Post;

import java.util.Optional;

public interface IPostService {
    void createPost(Post newPost) ;
    void deletePost(Long idPost);
    void findallPost(Post userPosts);
    Optional<Post> findByTitle(String title);
}
