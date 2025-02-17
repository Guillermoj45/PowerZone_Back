package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.Cloudinary.CloudinaryService;
import com.actividad_10.powerzone_back.Cloudinary.UploadResult;
import com.actividad_10.powerzone_back.Config.JwtService;
import com.actividad_10.powerzone_back.DTOs.CommentDetailsDto;
import com.actividad_10.powerzone_back.DTOs.Post2Dto;
import com.actividad_10.powerzone_back.DTOs.PostDto;
import com.actividad_10.powerzone_back.Entities.*;
import com.actividad_10.powerzone_back.Entities.emun.MultimediaType;
import com.actividad_10.powerzone_back.Repositories.*;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService implements IPostService {

    private final ImageRepository imageRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final LikePostRepository likePostRepository;
    private final BooksmarksRepository booksmarksRepository;
    private final CloudinaryService cloudinaryService;
    private final AddNotificationService addNotificationService;

    @Transactional
    @Override
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
        addNotificationService.createNotificationNewPost(savedPost);

        // Upload the image to Cloudinary and get the URL if the image is present
        if (image != null && !image.isEmpty()) {
            try {
                UploadResult uploadResult = cloudinaryService.uploadFilePV(image, "posts");
                Image img = new Image();
                img.setImage(uploadResult.getPublicId());
                img.setType(MultimediaType.valueOf(uploadResult.getResourceType().toUpperCase()));
                img.setPostCreatedAt(savedPost.getCreatedAt());
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
        post2Dto.setUserId(savedPost.getUser().getId());
        post2Dto.setDelete(savedPost.getDelete());

        // Get the user's profile
        String avatar = user.getProfile().getAvatar();
        String nickname = user.getProfile().getNickname();

        Long numcomments = postRepository.countCommentsByPostId(savedPost.getId());
        Long numlikes = postRepository.countLikesByPostId(savedPost.getId());
        Pageable pageable = PageRequest.of(0, 1);
        List<CommentDetailsDto> firstCommentDetailsList = postRepository.findFirstCommentDetailsByPostId(savedPost.getId(), pageable);
        Optional<CommentDetailsDto> firstCommentDetails = firstCommentDetailsList.isEmpty() ? Optional.empty() : Optional.of(firstCommentDetailsList.get(0));

        // Create and return the DTO
        PostDto postDto = new PostDto();
        if (firstCommentDetails.isPresent()) {
            CommentDetailsDto commentDetails = firstCommentDetails.get();
            postDto.setFirstcomment(commentDetails.getContent());
            postDto.setNicknamecomment(commentDetails.getNickname());
            postDto.setAvatarcomment(commentDetails.getAvatar());
        } else {
            postDto.setFirstcomment("Se el primero en comentar esta publicación");
            postDto.setNicknamecomment(" ");
            postDto.setAvatarcomment(" ");
        }
        postDto.setPost(post2Dto);
        postDto.setAvatar(avatar);
        postDto.setNickname(nickname);
        postDto.setNumcomments(numcomments);
        postDto.setNumlikes(numlikes);

        return postDto;
    }

    private Long extractUserIdFromEmail(String email) {
        return userRepository.findByEmail(email).get().getId();
    }

    private User extractUserFromEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
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

    public Post updatePost(Post post) {
        return postRepository.save(post);
    }

    public Post findaById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public List<PostDto> getPostsWithMostComments() {
        List<Post> posts = postRepository.findPostsWithMostComments();

        return posts.stream().map(post -> {
            Post2Dto post2Dto = new Post2Dto();
            post2Dto.setId(post.getId());
            post2Dto.setContent(post.getContent());
            post2Dto.setCreatedAt(post.getCreatedAt());
            post2Dto.setUserId(post.getUser().getId());
            post2Dto.setDelete(post.getDelete());

            User user = userRepository.findById(post.getUser().getId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            String avatar = user.getProfile().getAvatar();
            String nickname = user.getProfile().getNickname();

            Long numcomments = postRepository.countCommentsByPostId(post.getId());
            Long numlikes = postRepository.countLikesByPostId(post.getId());
            Pageable pageable = PageRequest.of(0, 1);
            List<CommentDetailsDto> firstCommentDetailsList = postRepository.findFirstCommentDetailsByPostId(post.getId(), pageable);
            Optional<CommentDetailsDto> firstCommentDetails = firstCommentDetailsList.isEmpty() ? Optional.empty() : Optional.of(firstCommentDetailsList.get(0));

            String cloudinaryBaseUrl = "https://res.cloudinary.com/dflz0gveu/image/upload/";
            Optional<Image> imageOptional = imageRepository.findByPostId(post.getId());
            String imagePost = imageOptional.map(img -> cloudinaryBaseUrl + img.getImage()).orElse(null);

            PostDto postDto = new PostDto(post2Dto, imagePost, avatar, nickname, numlikes, numcomments, null, null, null, post.getCreatedAt());
            if (firstCommentDetails.isPresent()) {
                CommentDetailsDto commentDetails = firstCommentDetails.get();
                postDto.setFirstcomment(commentDetails.getContent());
                postDto.setNicknamecomment(commentDetails.getNickname());
                postDto.setAvatarcomment(commentDetails.getAvatar());
            } else {
                postDto.setFirstcomment("Se el primero en comentar esta publicación");
                postDto.setNicknamecomment(" ");
                postDto.setAvatarcomment(" ");
            }

            return postDto;
        }).collect(Collectors.toList());
    }

    public List<PostDto> getPostsWithMostLikes() {
        List<Post> posts = postRepository.findPostsWithMostLikes();

        return posts.stream().map(post -> {
            Post2Dto post2Dto = new Post2Dto();
            post2Dto.setId(post.getId());
            post2Dto.setContent(post.getContent());
            post2Dto.setCreatedAt(post.getCreatedAt());
            post2Dto.setUserId(post.getUser().getId());
            post2Dto.setDelete(post.getDelete());

            User user = userRepository.findById(post.getUser().getId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            String avatar = user.getProfile().getAvatar();
            String nickname = user.getProfile().getNickname();

            Long numcomments = postRepository.countCommentsByPostId(post.getId());
            Long numlikes = postRepository.countLikesByPostId(post.getId());
            Pageable pageable = PageRequest.of(0, 1);
            List<CommentDetailsDto> firstCommentDetailsList = postRepository.findFirstCommentDetailsByPostId(post.getId(), pageable);
            Optional<CommentDetailsDto> firstCommentDetails = firstCommentDetailsList.isEmpty() ? Optional.empty() : Optional.of(firstCommentDetailsList.get(0));

            String cloudinaryBaseUrl = "https://res.cloudinary.com/dflz0gveu/image/upload/";
            Optional<Image> imageOptional = imageRepository.findByPostId(post.getId());
            String imagePost = imageOptional.map(img -> cloudinaryBaseUrl + img.getImage()).orElse(null);

            PostDto postDto = new PostDto(post2Dto, imagePost, avatar, nickname, numlikes, numcomments, null, null, null, post.getCreatedAt());
            if (firstCommentDetails.isPresent()) {
                CommentDetailsDto commentDetails = firstCommentDetails.get();
                postDto.setFirstcomment(commentDetails.getContent());
                postDto.setNicknamecomment(commentDetails.getNickname());
                postDto.setAvatarcomment(commentDetails.getAvatar());
            } else {
                postDto.setFirstcomment("Se el primero en comentar esta publicación");
                postDto.setNicknamecomment(" ");
                postDto.setAvatarcomment(" ");
            }

            return postDto;
        }).collect(Collectors.toList());
    }


    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAllPosts();

        return posts.stream().map(post -> {
            Post2Dto post2Dto = new Post2Dto();
            post2Dto.setId(post.getId());
            post2Dto.setContent(post.getContent());
            post2Dto.setCreatedAt(post.getCreatedAt());
            post2Dto.setUserId(post.getUser().getId());
            post2Dto.setDelete(post.getDelete());

            User user = userRepository.findById(post.getUser().getId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            String avatar = user.getProfile().getAvatar();
            String nickname = user.getProfile().getNickname();

            Long numcomments = postRepository.countCommentsByPostId(post.getId());
            Long numlikes = postRepository.countLikesByPostId(post.getId());
            Pageable pageable = PageRequest.of(0, 1);
            List<CommentDetailsDto> firstCommentDetailsList = postRepository.findFirstCommentDetailsByPostId(post.getId(), pageable);
            Optional<CommentDetailsDto> firstCommentDetails = firstCommentDetailsList.isEmpty() ? Optional.empty() : Optional.of(firstCommentDetailsList.getFirst());

            // Construcción de la URL de Cloudinary
            String cloudinaryBaseUrl = "https://res.cloudinary.com/dflz0gveu/image/upload/";

            Optional<Image> imageOptional = imageRepository.findByPostId(post.getId());
            String imagePost = imageOptional.map(img -> cloudinaryBaseUrl + img.getImage()).orElse(null);

            PostDto postDto = new PostDto(post2Dto, imagePost, avatar, nickname, numlikes, numcomments, null, null, null, post.getCreatedAt());
            if (firstCommentDetails.isPresent()) {
                CommentDetailsDto commentDetails = firstCommentDetails.get();
                postDto.setFirstcomment(commentDetails.getContent());
                postDto.setNicknamecomment(commentDetails.getNickname());
                postDto.setAvatarcomment(commentDetails.getAvatar());
            } else {
                postDto.setFirstcomment("Se el primero en comentar esta publicación");
                postDto.setNicknamecomment(" ");
                postDto.setAvatarcomment(" ");
            }

            return postDto;
        }).collect(Collectors.toList());
    }

    public List<PostDto> getAllSavedPosts(String token) {
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);

        Long userId = extractUserIdFromEmail(email);
        List<Long> savedPostIds = booksmarksRepository.findAllSavedPostIdsByUserId(userId);
        List<Post> savedPosts = postRepository.findAllById(savedPostIds);

        return savedPosts.stream().map(post -> {
            Post2Dto post2Dto = new Post2Dto();
            post2Dto.setId(post.getId());
            post2Dto.setContent(post.getContent());
            post2Dto.setCreatedAt(post.getCreatedAt());
            post2Dto.setUserId(post.getUser().getId());
            post2Dto.setDelete(post.getDelete());

            User user = userRepository.findById(post.getUser().getId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            String avatar = user.getProfile().getAvatar();
            String nickname = user.getProfile().getNickname();

            Long numcomments = postRepository.countCommentsByPostId(post.getId());
            Long numlikes = postRepository.countLikesByPostId(post.getId());
            Pageable pageable = PageRequest.of(0, 1);
            List<CommentDetailsDto> firstCommentDetailsList = postRepository.findFirstCommentDetailsByPostId(post.getId(), pageable);
            Optional<CommentDetailsDto> firstCommentDetails = firstCommentDetailsList.isEmpty() ? Optional.empty() : Optional.of(firstCommentDetailsList.get(0));

            String cloudinaryBaseUrl = "https://res.cloudinary.com/dflz0gveu/image/upload/";
            Optional<Image> imageOptional = imageRepository.findByPostId(post.getId());
            String imagePost = imageOptional.map(img -> cloudinaryBaseUrl + img.getImage()).orElse(null);

            PostDto postDto = new PostDto(post2Dto, imagePost, avatar, nickname, numlikes, numcomments, null, null, null, post.getCreatedAt());
            if (firstCommentDetails.isPresent()) {
                CommentDetailsDto commentDetails = firstCommentDetails.get();
                postDto.setFirstcomment(commentDetails.getContent());
                postDto.setNicknamecomment(commentDetails.getNickname());
                postDto.setAvatarcomment(commentDetails.getAvatar());
            } else {
                postDto.setFirstcomment("Se el primero en comentar esta publicación");
                postDto.setNicknamecomment(" ");
                postDto.setAvatarcomment(" ");
            }

            return postDto;
        }).collect(Collectors.toList());
    }
    public List<PostDto> getPostsByPattern(String pattern) {
        List<Post> posts = postRepository.findPostsByPattern(pattern);

        return posts.stream().map(post -> {
            Post2Dto post2Dto = new Post2Dto();
            post2Dto.setId(post.getId());
            post2Dto.setContent(post.getContent());
            post2Dto.setCreatedAt(post.getCreatedAt());
            post2Dto.setUserId(post.getUser().getId());
            post2Dto.setDelete(post.getDelete());

            User user = userRepository.findById(post.getUser().getId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            String avatar = user.getProfile().getAvatar();
            String nickname = user.getProfile().getNickname();

            Long numcomments = postRepository.countCommentsByPostId(post.getId());
            Long numlikes = postRepository.countLikesByPostId(post.getId());
            Pageable pageable = PageRequest.of(0, 1);
            List<CommentDetailsDto> firstCommentDetailsList = postRepository.findFirstCommentDetailsByPostId(post.getId(), pageable);
            Optional<CommentDetailsDto> firstCommentDetails = firstCommentDetailsList.isEmpty() ? Optional.empty() : Optional.of(firstCommentDetailsList.get(0));

            String cloudinaryBaseUrl = "https://res.cloudinary.com/dflz0gveu/image/upload/";
            Optional<Image> imageOptional = imageRepository.findByPostId(post.getId());
            String imagePost = imageOptional.map(img -> cloudinaryBaseUrl + img.getImage()).orElse(null);

            PostDto postDto = new PostDto(post2Dto, imagePost, avatar, nickname, numlikes, numcomments, null, null, null, post.getCreatedAt());
            if (firstCommentDetails.isPresent()) {
                CommentDetailsDto commentDetails = firstCommentDetails.get();
                postDto.setFirstcomment(commentDetails.getContent());
                postDto.setNicknamecomment(commentDetails.getNickname());
                postDto.setAvatarcomment(commentDetails.getAvatar());
            } else {
                postDto.setFirstcomment("Se el primero en comentar esta publicación");
                postDto.setNicknamecomment(" ");
                postDto.setAvatarcomment(" ");
            }

            return postDto;
        }).collect(Collectors.toList());
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
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        addNotificationService.createNotificationLike(post, user.getProfile());
        LikePost likePost = new LikePost();
        likePost.setUser(user.getProfile());
        likePost.setPost(post);
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

    public boolean hasUserLikedPost(String token, Long postId) {
        // Extract user data from the token
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);

        // Get the user ID from the email
        Long userId = extractUserIdFromEmail(email);

        // Check if the user has liked the post
        return likePostRepository.existsByUserIdAndPostId(userId, postId);
    }

    public boolean hasUserSavedPost(String token, Long postId) {
        // Extract user data from the token
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);

        // Get the user ID from the email
        Long userId = extractUserIdFromEmail(email);

        // Check if the user has saved the post
        return booksmarksRepository.existsByUserIdAndPostId(userId, postId);
    }

    public PostDto getPostById(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (!postOptional.isPresent()) {
            throw new RuntimeException("Post not found");
        }
        Post post = postOptional.get();

        // Create Post2Dto
        Post2Dto post2Dto = new Post2Dto();
        post2Dto.setId(post.getId());
        post2Dto.setContent(post.getContent());
        post2Dto.setCreatedAt(post.getCreatedAt());
        post2Dto.setUserId(post.getUser().getId());
        post2Dto.setDelete(post.getDelete());

        // Get the user's profile
        User user = userRepository.findById(post.getUser().getId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        String avatar = user.getProfile().getAvatar();
        String nickname = user.getProfile().getNickname();

        Long numcomments = postRepository.countCommentsByPostId(post.getId());
        Long numlikes = postRepository.countLikesByPostId(post.getId());
        Pageable pageable = PageRequest.of(0, 1);
        List<CommentDetailsDto> firstCommentDetailsList = postRepository.findFirstCommentDetailsByPostId(post.getId(), pageable);
        Optional<CommentDetailsDto> firstCommentDetails = firstCommentDetailsList.isEmpty() ? Optional.empty() : Optional.of(firstCommentDetailsList.get(0));

        // Construcción de la URL de Cloudinary
        String cloudinaryBaseUrl = "https://res.cloudinary.com/dflz0gveu/image/upload/";

        Optional<Image> imageOptional = imageRepository.findByPostId(post.getId());
        String imagePost = imageOptional.map(img -> cloudinaryBaseUrl + img.getImage()).orElse(null);

        // Create and return the DTO
        PostDto postDto = new PostDto(post2Dto, imagePost, avatar, nickname, numlikes, numcomments, null, null, null, post.getCreatedAt());
        if (firstCommentDetails.isPresent()) {
            CommentDetailsDto commentDetails = firstCommentDetails.get();
            postDto.setFirstcomment(commentDetails.getContent());
            postDto.setNicknamecomment(commentDetails.getNickname());
            postDto.setAvatarcomment(commentDetails.getAvatar());
        } else {
            postDto.setFirstcomment("Se el primero en comentar esta publicación");
            postDto.setNicknamecomment(" ");
            postDto.setAvatarcomment(" ");
        }

        return postDto;
    }

    public List<PostDto> getUserPosts(String token) {
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);

        Long userId = extractUserIdFromEmail(email);
        List<Post> userPosts = postRepository.findAllByUserId(userId);

        return userPosts.stream().map(post -> {
            Post2Dto post2Dto = new Post2Dto();
            post2Dto.setId(post.getId());
            post2Dto.setContent(post.getContent());
            post2Dto.setCreatedAt(post.getCreatedAt());
            post2Dto.setUserId(post.getUser().getId());
            post2Dto.setDelete(post.getDelete());

            User user = userRepository.findById(post.getUser().getId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            String avatar = user.getProfile().getAvatar();
            String nickname = user.getProfile().getNickname();

            Long numcomments = postRepository.countCommentsByPostId(post.getId());
            Long numlikes = postRepository.countLikesByPostId(post.getId());
            Pageable pageable = PageRequest.of(0, 1);
            List<CommentDetailsDto> firstCommentDetailsList = postRepository.findFirstCommentDetailsByPostId(post.getId(), pageable);
            Optional<CommentDetailsDto> firstCommentDetails = firstCommentDetailsList.isEmpty() ? Optional.empty() : Optional.of(firstCommentDetailsList.get(0));

            String cloudinaryBaseUrl = "https://res.cloudinary.com/dflz0gveu/image/upload/";
            Optional<Image> imageOptional = imageRepository.findByPostId(post.getId());
            String imagePost = imageOptional.map(img -> cloudinaryBaseUrl + img.getImage()).orElse(null);

            PostDto postDto = new PostDto(post2Dto, imagePost, avatar, nickname, numlikes, numcomments, null, null, null, post.getCreatedAt());
            if (firstCommentDetails.isPresent()) {
                CommentDetailsDto commentDetails = firstCommentDetails.get();
                postDto.setFirstcomment(commentDetails.getContent());
                postDto.setNicknamecomment(commentDetails.getNickname());
                postDto.setAvatarcomment(commentDetails.getAvatar());
            } else {
                postDto.setFirstcomment("Se el primero en comentar esta publicación");
                postDto.setNicknamecomment(" ");
                postDto.setAvatarcomment(" ");
            }

            return postDto;
        }).collect(Collectors.toList());
    }

    public List<PostDto> getPostsByUserId(Long userId) {
        List<Post> userPosts = postRepository.findAllByUserId(userId);

        return userPosts.stream().map(post -> {
            Post2Dto post2Dto = new Post2Dto();
            post2Dto.setId(post.getId());
            post2Dto.setContent(post.getContent());
            post2Dto.setCreatedAt(post.getCreatedAt());
            post2Dto.setUserId(post.getUser().getId());
            post2Dto.setDelete(post.getDelete());

            User user = userRepository.findById(post.getUser().getId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            String avatar = user.getProfile().getAvatar();
            String nickname = user.getProfile().getNickname();

            Long numcomments = postRepository.countCommentsByPostId(post.getId());
            Long numlikes = postRepository.countLikesByPostId(post.getId());
            Pageable pageable = PageRequest.of(0, 1);
            List<CommentDetailsDto> firstCommentDetailsList = postRepository.findFirstCommentDetailsByPostId(post.getId(), pageable);
            Optional<CommentDetailsDto> firstCommentDetails = firstCommentDetailsList.isEmpty() ? Optional.empty() : Optional.of(firstCommentDetailsList.get(0));

            String cloudinaryBaseUrl = "https://res.cloudinary.com/dflz0gveu/image/upload/";
            Optional<Image> imageOptional = imageRepository.findByPostId(post.getId());
            String imagePost = imageOptional.map(img -> cloudinaryBaseUrl + img.getImage()).orElse(null);

            PostDto postDto = new PostDto(post2Dto, imagePost, avatar, nickname, numlikes, numcomments, null, null, null, post.getCreatedAt());
            if (firstCommentDetails.isPresent()) {
                CommentDetailsDto commentDetails = firstCommentDetails.get();
                postDto.setFirstcomment(commentDetails.getContent());
                postDto.setNicknamecomment(commentDetails.getNickname());
                postDto.setAvatarcomment(commentDetails.getAvatar());
            } else {
                postDto.setFirstcomment("Se el primero en comentar esta publicación");
                postDto.setNicknamecomment(" ");
                postDto.setAvatarcomment(" ");
            }

            return postDto;
        }).collect(Collectors.toList());
    }

    public List<PostDto> getPostsFromFollowedUsers(String token) {
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);

        Long userId = extractUserIdFromEmail(email);
        List<Long> followedUserIds = userRepository.findFollowedUserIdsByUserId(userId);
        List<Post> followedUserPosts = postRepository.findAllByUserIdIn(followedUserIds);

        return followedUserPosts.stream().map(post -> {
            Post2Dto post2Dto = new Post2Dto();
            post2Dto.setId(post.getId());
            post2Dto.setContent(post.getContent());
            post2Dto.setCreatedAt(post.getCreatedAt());
            post2Dto.setUserId(post.getUser().getId());
            post2Dto.setDelete(post.getDelete());

            User user = userRepository.findById(post.getUser().getId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            String avatar = user.getProfile().getAvatar();
            String nickname = user.getProfile().getNickname();

            Long numcomments = postRepository.countCommentsByPostId(post.getId());
            Long numlikes = postRepository.countLikesByPostId(post.getId());
            Pageable pageable = PageRequest.of(0, 1);
            List<CommentDetailsDto> firstCommentDetailsList = postRepository.findFirstCommentDetailsByPostId(post.getId(), pageable);
            Optional<CommentDetailsDto> firstCommentDetails = firstCommentDetailsList.isEmpty() ? Optional.empty() : Optional.of(firstCommentDetailsList.get(0));

            String cloudinaryBaseUrl = "https://res.cloudinary.com/dflz0gveu/image/upload/";
            Optional<Image> imageOptional = imageRepository.findByPostId(post.getId());
            String imagePost = imageOptional.map(img -> cloudinaryBaseUrl + img.getImage()).orElse(null);

            PostDto postDto = new PostDto(post2Dto, imagePost, avatar, nickname, numlikes, numcomments, null, null, null, post.getCreatedAt());
            if (firstCommentDetails.isPresent()) {
                CommentDetailsDto commentDetails = firstCommentDetails.get();
                postDto.setFirstcomment(commentDetails.getContent());
                postDto.setNicknamecomment(commentDetails.getNickname());
                postDto.setAvatarcomment(commentDetails.getAvatar());
            } else {
                postDto.setFirstcomment("Se el primero en comentar esta publicación");
                postDto.setNicknamecomment(" ");
                postDto.setAvatarcomment(" ");
            }

            return postDto;
        }).collect(Collectors.toList());
    }

    public Long getUserIdByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        return post.getUser().getId();
    }
    public void sharePost(String token, Long postId) {
    }
}
