package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.Entities.Post;
import com.actividad_10.powerzone_back.Repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostService implements IPostService {
    @Autowired
    private PostRepository postRepository;

//    @Override
//    public void createPost(int idUser) {
//        Post post = new Post();
//        post.setUserId((long) idUser);
//        post.setCreatedAt(java.time.LocalDate.now());
//        postRepository.createPost(idUser);
//    }

    @Override
    public void createPost(int idUser) {

    }

    @Override
    public void deletePost(int idPost) {

    }

//    @Override
//    public void deletePost(int idPost) {
//        postRepository.deletePost(idPost);
//    }


    @Override
    public Optional<Post> findByTitle(String namePost) {
        return postRepository.findByTitle(namePost);
    }

}
