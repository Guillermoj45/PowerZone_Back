package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.Cloudinary.CloudinaryService;
import com.actividad_10.powerzone_back.Config.JwtService;
import com.actividad_10.powerzone_back.DTOs.Post2Dto;
import com.actividad_10.powerzone_back.DTOs.PostDto;
import com.actividad_10.powerzone_back.Entities.*;
import com.actividad_10.powerzone_back.Entities.emun.MultimediaType;
import com.actividad_10.powerzone_back.Repositories.*;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService implements IPostService {

    private final ImageRepository imageRepository;
    private PostRepository postRepository;

    private UserRepository userRepository;

    private final JwtService jwtService;

    private LikePostRepository likePostRepository;

    private BooksmarksRepository booksmarksRepository;

    private final CloudinaryService cloudinaryService;
    @Override
    @Transactional
    public PostDto createPost(String token, Post newPost, MultipartFile image) {
        // Extract user data from the token
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);

        // Get the user ID from the email
        User user = extractUserFromEmail(email);

        // Set user and creation date for the new post
        newPost.setUser(user);
        newPost.setCreatedAt(LocalDateTime.now());
        newPost.setDelete(false);

        // Save the new post first
        Post savedPost = postRepository.save(newPost);

        // Upload the image to Cloudinary and get the URL if the image is present
        if (image != null && !image.isEmpty()) {
            try {
                String imageUrl = cloudinaryService.uploadFile(image, "posts");
                Image img = new Image();
                img.setImage(imageUrl);
                img.setType(MultimediaType.IMAGE);
                img.setPostCreatedAt(LocalDateTime.now());
                img.setPost(savedPost);

                imageRepository.save(img);
            } catch (IOException e) {
                throw new RuntimeException("Error uploading the image", e);
            }
        }

        // Create Post2Dto
        Post2Dto post2Dto = new Post2Dto();
        post2Dto.setId(savedPost.getId());
        post2Dto.setContent(savedPost.getContent());
        post2Dto.setCreatedAt(savedPost.getCreatedAt());
        post2Dto.setImages(savedPost.getImages());
        post2Dto.setUserId(savedPost.getUser().getId());
        post2Dto.setDelete(savedPost.getDelete());

        // Get the user's profile
        String avatar = user.getProfile().getAvatar();
        String nickname = user.getProfile().getNickname();

        // Create and return the DTO
        return new PostDto(post2Dto, avatar, nickname);
    }
    private Long extractUserIdFromEmail(String email) {
        return userRepository.findByEmail(email).get().getId();
    }

    private User extractUserFromEmail(String email) {
        return userRepository.findByEmail(email).get();
    }

    @Transactional
    @Override
    public void deletePost(String token, Post deletePost) {
        // Extraer datos del usuario desde el token
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);
        String rol = claims.get("rol", String.class);

        // Obtener el ID del usuario a partir del email
        User user = extractUserFromEmail(email);

        // Verificar que el post pertenece al usuario
        if (deletePost.getUser().getId().equals(user.getId())) {
            postRepository.deleteById(deletePost.getId());
        } else if(rol.equals("ADMIN")) {
            deletePost.setDelete(true);
            postRepository.save(deletePost);
        }else  {
                throw new RuntimeException("No tienes permiso para eliminar este post");
            }

    }

    public Post findaById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public void findallPost(Post userPosts) {
        postRepository.findAll();
    }

    public List<Post> findbestPost() {
        return postRepository.findPostsWithMostLikes();

    }

    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        Post2Dto post2Dto = new Post2Dto();
        post2Dto.setId(posts.get(0).getId());
        post2Dto.setContent(posts.get(0).getContent());
        post2Dto.setCreatedAt(posts.get(0).getCreatedAt());
        post2Dto.setImages(posts.get(0).getImages());
        post2Dto.setUserId(posts.get(0).getUser().getId());
        post2Dto.setDelete(posts.get(0).getDelete());
        return posts.stream().map(post -> {
            User user = userRepository.findById(post.getUser().getId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            String avatar = user.getProfile().getAvatar();
            String nickname = user.getProfile().getNickname();
            return new PostDto(post2Dto, avatar, nickname);
        }).collect(Collectors.toList());
    }

    public List<Post> finduserPost(String token) {
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);


        // Obtener el ID del usuario a partir del email
        Long userId = extractUserIdFromEmail(email);
        return postRepository.findAllByUserId(userId);
    }

    public void savePost(String token, Post post) {
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);

        Long userId = extractUserIdFromEmail(email);
        Booksmarks booksmarks = new Booksmarks();
        booksmarks.setUserId(userId);
        booksmarks.setPostId(post.getId());
        booksmarks.setCreatedAtPost(postRepository.findCreatedAtById(post.getId()).get());
        booksmarksRepository.save(booksmarks);

    }

    @Override
    public void unsavePost(String token, Post post) {
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);

        Long userId = extractUserIdFromEmail(email);
        booksmarksRepository.deleteById(userId, post.getId());
    }

    public void likePost(String token, Post post) {
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);

        Long userId = extractUserIdFromEmail(email);
        LikePost likePost = new LikePost();
        likePost.setUserId(userId);
        likePost.setPostId(post.getId());
        likePost.setCreatedAtPost(postRepository.findCreatedAtById(post.getId()).get());
        likePostRepository.save(likePost);

    }
    public void unlikePost(String token, Post post) {
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);

        Long userId = extractUserIdFromEmail(email);
        likePostRepository.deleteById(userId, post.getId());

    }

    public void sharePost(String token, Long postId) {
    }


}
