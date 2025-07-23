package com.strawhats.blogplatform.security.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

    @Value("${application.jwt.secret}")
    private String jwtSecret;

    @Value("${application.jwt.expiration.ms}")
    private Long jwtExpirationMs;

    public String getJwtTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Boolean validateJwtToken(String jwtToken) {
        try {
            log.info("Validating Jwt token ...");
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(jwtToken);
            return true;
        } catch (JwtException e) {
            log.error("Jwt Exception occured while validating Jwt token. Error {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("IllegalArgumentException occured while validating Jwt token. Error {}", e.getMessage());
        }
        return false;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String generateJwtTokenFromUsername(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }
}
