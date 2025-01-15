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

   @Override
   public void createPost(Post newPost) {
       Post post = new Post();
       post.setUserId(newPost.getUserId());
       post.setTitle(newPost.getTitle());
       post.setContent(newPost.getContent());
       post.setCreatedAt(java.time.LocalDate.now());
       postRepository.save(post);
   }


    @Override
    public void deletePost(Long idPost) {
        postRepository.deleteById(idPost);
    }

    public void findallPost(Post userPosts) {
        postRepository.findAll();
    }
    @Override
    public Optional<Post> findByTitle(String namePost) {
        return postRepository.findByTitle(namePost);
    }

}
