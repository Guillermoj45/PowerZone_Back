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

@Service
@AllArgsConstructor
public class UserService implements IUserService, UserDetailsService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public void createUser(CreacionPerfilDto nuevoPerfil) {
        // Validar datos del DTO
        if (nuevoPerfil.getEmail() == null || nuevoPerfil.getPassword() == null || nuevoPerfil.getName() == null) {
            throw new BlankInfo("Email, password y nombre son obligatorios.");
        }

        // Verificar si el email ya existe
        if (userRepository.findByEmail(nuevoPerfil.getEmail()).isPresent()) {
            throw new ExistingField("El email ya está en uso.");
        }

        // Crear usuario
        User user = new User();
        user.setEmail(nuevoPerfil.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(nuevoPerfil.getPassword()));
        //Como todos son ususarios a no ser que lo modifiquemos en la base de datos, lo hardcodeamos
        user.setRole(Rol.USER);

        // Crear perfil asociado
        Profile profile = new Profile();
        profile.setName(nuevoPerfil.getName());
        profile.setAvatar(nuevoPerfil.getAvatar() != null ? nuevoPerfil.getAvatar() :
                "https://res.cloudinary.com/dflz0gveu/image/upload/v1718394870/avatars/default.png");
        profile.setBornDate(nuevoPerfil.getBornDate());
        profile.setCreatedAt(LocalDate.now());
        profile.setActivo(nuevoPerfil.getActivo() != null ? nuevoPerfil.getActivo() : true);

        // Asignar el perfil al usuario
        user.setProfile(profile);

        // Guardar usuario (el perfil se guarda automáticamente)
        userRepository.save(user);
    }

    @Override
    public ResponseEntity<RespuestaDto> LoginUser(LoginDto loginDto) {

        if (loginDto.getEmail() == null || loginDto.getPassword() == null) {
            throw new BlankInfo("Email y password son obligatorios.");
        }

        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new BlankInfo("Email o password incorrectos."));

        if (!new BCryptPasswordEncoder().matches(loginDto.getPassword(), user.getPassword())) {
            throw new BlankInfo("Email o password incorrectos.");
        }

        // Generate token
        String token = jwtService.generateToken(user);

        // Build the response
        RespuestaDto respuesta = RespuestaDto.builder()
                .estado(HttpStatus.OK.value())
                .token(token)
                .build();

        return ResponseEntity.ok(respuesta);
    }

    //TODO
    @Override
    public Profile2Dto returnProfile(String token) {
        // Remove the "Bearer " prefix if it exists
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }

        TokenDto tokenDto = jwtService.extractTokenData(token);
        return null;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElse(null);
    }


}
