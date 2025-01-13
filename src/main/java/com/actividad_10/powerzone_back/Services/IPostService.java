package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.Entities.Post;

import java.util.Optional;

public interface IPostService {
    void createPost(int idUser);
    void deletePost(int idPost);

    Optional<Post> findByTitle(String title);
}
