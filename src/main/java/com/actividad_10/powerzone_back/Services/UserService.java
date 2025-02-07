package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.Cloudinary.CloudinaryService;
import com.actividad_10.powerzone_back.Config.JwtService;
import com.actividad_10.powerzone_back.DTOs.*;
import com.actividad_10.powerzone_back.Entities.Profile;
import com.actividad_10.powerzone_back.Entities.emun.Rol;
import com.actividad_10.powerzone_back.Entities.User;
import com.actividad_10.powerzone_back.Exceptions.BlankInfo;
import com.actividad_10.powerzone_back.Exceptions.ExistingField;
import com.actividad_10.powerzone_back.Repositories.ProfileRepository;
import com.actividad_10.powerzone_back.Repositories.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements IUserService, UserDetailsService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final CloudinaryService cloudinaryService;
    private final ProfileRepository profileRepository;



    private boolean isEmailPasswordNull(String email, String password){
        return email == null || password == null;
    }

    @Override
    @Transactional
    public void createUser(CreacionPerfilDto nuevoPerfil) {
        validateEmailAndPassword(nuevoPerfil.getEmail(), nuevoPerfil.getPassword());
        checkIfEmailExists(nuevoPerfil.getEmail());

        User user = null;
        try {
            user = buildUser(nuevoPerfil);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        userRepository.save(user);
    }

    private void validateEmailAndPassword(String email, String password) {
        if (isEmailPasswordNull(email, password)) {
            throw new BlankInfo("Email y password son obligatory.");
        }
    }

    private void checkIfEmailExists(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ExistingField("El email ya está en uso.");
        }
    }

    private User buildUser(CreacionPerfilDto nuevoPerfil) throws IOException {
        User user = new User();
        user.setEmail(nuevoPerfil.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(nuevoPerfil.getPassword()));
        user.setRole(Rol.USER);

        Profile profile = buildProfile(nuevoPerfil);
        user.setProfile(profile);

        return user;
    }

    private Profile buildProfile(CreacionPerfilDto nuevoPerfil) throws IOException {
        Profile profile = new Profile();
        profile.setName(nuevoPerfil.getName());
        profile.setNickname(nuevoPerfil.getNickname());

        // Subir el avatar (Base64 o MultipartFile)
        String img = cloudinaryService.uploadFile(nuevoPerfil.getAvatar(), "avatar");

        profile.setAvatar(img);

        profile.setBornDate(nuevoPerfil.getBornDate());
        profile.setCreatedAt(LocalDate.now());
        profile.setActivo(nuevoPerfil.getActivo() != null ? nuevoPerfil.getActivo() : true);

        return profile;
    }

    @Override
    public ResponseEntity<RespuestaDto> LoginUser(LoginDto loginDto) {
        validateEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new BlankInfo("Email o password incorrectos."));
        isPasswordValid(loginDto, user);
        // Generate token
        String token = jwtService.generateToken(user);

        // Build the response
        RespuestaDto respuesta = RespuestaDto.builder()
                .estado(HttpStatus.OK.value())
                .token(token)
                .build();
        return ResponseEntity.ok(respuesta);
    }

    private static void isPasswordValid(LoginDto loginDto, User user) {
        if (!new BCryptPasswordEncoder().matches(loginDto.getPassword(), user.getPassword())) {
            throw new BlankInfo("Email o password incorrectos.");
        }
    }

    private Profile2Dto getProfile2Dto(String token) {
        TokenDto tokenDto = jwtService.extractTokenData(token);
        User user = (User) loadUserByUsername(tokenDto.getEmail());
        Profile2Dto profile2Dto = new Profile2Dto();
        profile2Dto.setId(user.getId());
        profile2Dto.setName(user.getProfile().getName());
        profile2Dto.setEmail(user.getEmail());
        profile2Dto.setAvatar(user.getProfile().getAvatar());
        profile2Dto.setBornDate(user.getProfile().getBornDate());
        profile2Dto.setNickName(user.getProfile().getNickname());
        profile2Dto.setFollowers(profileRepository.countFollowersByProfileId(user.getProfile().getId()));
        profile2Dto.setFollowing(profileRepository.countFollowingByProfileId(user.getProfile().getId()));
        return profile2Dto;
    }

    //Metodo que me devuelve un perfil concreto
    @Override
    public Profile2Dto returnProfile(String token) {
        token = jwtService.desEncriptToken(token);
        return getProfile2Dto(token);
    }


    //Metodo que me encuentra el email
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElse(null);
    }

    public boolean exists(String email){
        return userRepository.existsUserByEmail(email);
    }

    public void updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + email));
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);
    }

    public void updateProfile(String token, Profile2Dto profile2Dto) throws IOException {
        token = jwtService.desEncriptToken(token);
        TokenDto tokenDto = jwtService.extractTokenData(token);
        User user = (User) loadUserByUsername(tokenDto.getEmail());

        Profile profile = user.getProfile();

        if (profile2Dto.getName() == null || profile2Dto.getName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        profile.setName(profile2Dto.getName());

        String img = cloudinaryService.uploadFile(profile2Dto.getAvatar(), "avatar");
        if (img != null)
            profile.setAvatar(img);

        profile.setBornDate(profile2Dto.getBornDate());
        profile.setNickname(profile2Dto.getNickName());

        userRepository.save(user);
    }

    @Transactional
    public void tutorialDone(String token) {
        token = jwtService.desEncriptToken(token);
        TokenDto tokenDto = jwtService.extractTokenData(token);
        User user = userRepository.findByEmail(tokenDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + tokenDto.getEmail()));

        user.getProfile().setNewUser(false);
        userRepository.save(user);
    }

    public boolean isTutorialDone(String token) {
        token = jwtService.desEncriptToken(token);
        TokenDto tokenDto = jwtService.extractTokenData(token);
        User user = userRepository.findByEmail(tokenDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + tokenDto.getEmail()));

        return user.getProfile().isNewUser();
    }
    // Seguir a un usuario
    @Transactional
    public boolean followUser(String token, Long followUserId) {
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);

        Long userId = extractUserIdFromEmail(email);
        Optional<UserIdDto> userOptional = userRepository.findByUserId(userId);
        Optional<UserIdDto> followUserOptional = userRepository.findByUserId(followUserId);

        if (userOptional.isPresent() && followUserOptional.isPresent()) {
            userRepository.followUser(userId, followUserId);
            return true;
        }
        return false;
    }



    @Transactional
    public boolean unfollowUser(String token, Long unfollowUserId) {
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);

        Long userId = extractUserIdFromEmail(email);
        Optional<UserIdDto> userOptional = userRepository.findByUserId(userId);
        Optional<UserIdDto> unfollowUserOptional = userRepository.findByUserId(unfollowUserId);

        if (userOptional.isPresent() && unfollowUserOptional.isPresent()) {
            userRepository.unfollowUser(userId, unfollowUserId);
            return true;
        }
        return false;
    }

    public boolean isFollowing(String token, Long followUserId) {
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.extractDatosToken(jwt);
        String email = claims.get("email", String.class);

        Long userId = extractUserIdFromEmail(email);
        return userRepository.isFollowing(userId, followUserId);
    }

    private Long extractUserIdFromEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + email));
        return user.getId();
    }
}
