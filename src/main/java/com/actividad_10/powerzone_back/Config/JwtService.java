package com.actividad_10.powerzone_back.Config;


import com.actividad_10.powerzone_back.DTOs.TokenDto;
import com.actividad_10.powerzone_back.Entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${JWT_SECRET}")
    private String secretKey;

    public String generateToken(User user){
            TokenDto tokenDto = TokenDto
                    .builder()
                    .email(user.getEmail())
                    .rol(user.getRole().name())
                    .fecha_creacion(System.currentTimeMillis())
                    .fecha_expiracion(System.currentTimeMillis() + 1000 * 60 * 60)
                    .build();

        return Jwts
                .builder()
                .claim("tokenDto", tokenDto)
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    //Me extrae todo el token
    private Claims extractDatosToken(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //Me extrea los datos que queremos
    public TokenDto extractTokenData(String token) {
        Claims claims = extractDatosToken(token);
        Map<String, Object> mapa = (LinkedHashMap<String, Object>) claims.get("tokenDto");

        return TokenDto.builder()
                .email((String) mapa.get("email"))
                .rol((String) mapa.get("rol"))
                .fecha_creacion((Long) mapa.get("fecha_creacion"))
                .fecha_expiracion((Long) mapa.get("fecha_expiracion"))
                .build();
    }

    public boolean isExpired(String token){
        return new Date(extractTokenData(token).getFecha_expiracion()).before(new Date());
    }

    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}