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

    //Método que me genera el token
    public String generateToken(User user){
        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("email", user.getEmail());
        claims.put("rol", user.getRole().name());
        claims.put("fecha_creacion", System.currentTimeMillis());
        claims.put("fecha_expiracion", System.currentTimeMillis() + 1000 * 60 * 60 * 12);

        return Jwts
                .builder()
                .setClaims(claims)
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }


    // Función extraer los datos del todo el token
    public Claims extractDatosToken(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Función que me extrae los datos del usuario eliminando toda la seguridad y header del token
    public TokenDto extractTokenData(String token) {
        token = token.trim();

        Claims claims = extractDatosToken(token);

        return TokenDto.builder()
                .email((String) claims.get("email"))
                .rol((String) claims.get("rol"))
                .fecha_creacion((Long) claims.get("fecha_creacion"))
                .fecha_expiracion((Long) claims.get("fecha_expiracion"))
                .build();
    }

    //Método que me comprueba si el token está expirado
    public boolean isExpired(String token){
        return new Date(extractTokenData(token).getFecha_expiracion()).before(new Date());
    }

    //Metodo que me decodifica el token
    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}