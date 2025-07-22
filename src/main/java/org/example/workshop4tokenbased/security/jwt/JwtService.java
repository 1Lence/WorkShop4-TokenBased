package org.example.workshop4tokenbased.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.workshop4tokenbased.dto.JwtAuthDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {
    //TODO: Убрать из кода
    @Value("43f0dc29f0d139154fa90aa78f9483f9ead546053f30ee978936e2aea9eec8090c1a20695ca59e486d8e8c90")
    private String jwtSecret;

    public JwtAuthDto generateAuthToken(String login) {
        return JwtAuthDto.builder()
                .token(generateJwtToken(login))
                .refreshToken(generateRefreshToken(login))
                .build();
    }

    public String getLoginFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    //TODO: Переделать в ControllerAdvice
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        }catch (ExpiredJwtException e){
            log.error("Expired JWT token {}", e.getMessage());
        }catch (UnsupportedJwtException e){
            log.error("Unsupported JWT token {}", e.getMessage());
        }catch (MalformedJwtException e){
            log.error("Malformed JWT token {}", e.getMessage());
        }catch (SecurityException e){
            log.error("Security exception {}", e.getMessage());
        }catch (Exception e){
            log.error("Invalid token {}", e.getMessage());
        }
        return false;
    }

    public JwtAuthDto refreshBaseToken(String login,String refreshToken) {
        return JwtAuthDto.builder()
                .token(generateJwtToken(login))
                .refreshToken(refreshToken)
                .build();
    }

    private String generateJwtToken(String login) {
        Date date = Date.from(LocalDateTime.now().plusMinutes(1).atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .subject(login)
                .expiration(date)
                .signWith(getSignInKey())
                .compact();
    }

    private String generateRefreshToken(String login) {
        Date date = Date.from(LocalDateTime.now().plusMinutes(15).atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .subject(login)
                .expiration(date)
                .signWith(getSignInKey())
                .compact();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}