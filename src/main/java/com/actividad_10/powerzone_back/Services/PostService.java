package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.Entities.Post;
import com.actividad_10.powerzone_back.Entities.User;
import com.actividad_10.powerzone_back.Repositories.PostRepository;
import com.actividad_10.powerzone_back.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostService implements IPostService {
    @Autowired
    private PostRepository postRepository;

    private UserRepository userRepository;

   @Override
   public void createPost(Post newPost) {
       newPost.setCreatedAt(java.time.LocalDate.now());
       postRepository.save(newPost);
   }
    public Long extractUserPosts(String userPosts) {
        Optional<User> user1 = userRepository.findByEmail(userPosts);
        Long userID = user1.get().getId();
        return userID;

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
