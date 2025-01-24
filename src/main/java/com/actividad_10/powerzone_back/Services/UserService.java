package com.actividad_10.powerzone_back.Services;

import com.actividad_10.powerzone_back.Config.JwtService;
import com.actividad_10.powerzone_back.DTOs.*;
import com.actividad_10.powerzone_back.Entities.Profile;
import com.actividad_10.powerzone_back.Entities.emun.Rol;
import com.actividad_10.powerzone_back.Entities.User;
import com.actividad_10.powerzone_back.Exceptions.BlankInfo;
import com.actividad_10.powerzone_back.Exceptions.ExistingField;
import com.actividad_10.powerzone_back.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements IUserService, UserDetailsService {

    private final UserRepository userRepository;
    private final JwtService jwtService;


    private boolean isEmailPasswordNull(String email, String password){
        return email == null || password == null;
    }

    @Override
    @Transactional
    public void createUser(CreacionPerfilDto nuevoPerfil) {
        validateEmailAndPassword(nuevoPerfil.getEmail(), nuevoPerfil.getPassword());
        checkIfEmailExists(nuevoPerfil.getEmail());

        User user = buildUser(nuevoPerfil);
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

    private User buildUser(CreacionPerfilDto nuevoPerfil) {
        User user = new User();
        user.setEmail(nuevoPerfil.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(nuevoPerfil.getPassword()));
        user.setRole(Rol.USER);

        Profile profile = buildProfile(nuevoPerfil);
        user.setProfile(profile);

        return user;
    }

    private Profile buildProfile(CreacionPerfilDto nuevoPerfil) {
        Profile profile = new Profile();
        profile.setName(nuevoPerfil.getName());
        profile.setNickname(nuevoPerfil.getNickname());
        profile.setAvatar(nuevoPerfil.getAvatar() != null ? nuevoPerfil.getAvatar() :
                "https://res.cloudinary.com/dflz0gveu/image/upload/v1718394870/avatars/default.png");
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
        profile2Dto.setBornDate(user.getProfile().getBornDate());

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

}
