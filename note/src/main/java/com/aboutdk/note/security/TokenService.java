package com.aboutdk.note.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
@Slf4j
public class TokenService {

    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long tokenValidityInMilliseconds = 7 * 24 * 60 * 60 * 1000;

    public String generateToken(String username) {
        long now = (new Date()).getTime();

        String jws = Jwts
                .builder()
                .setSubject(username)
                .signWith(key)
                .setExpiration(new Date(now + tokenValidityInMilliseconds))
                .compact();
        return jws;
    }

    public boolean validateToken(String jws) {
        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
        log.info("[token string]: {}", claimsJws.toString());
        return true;
    }

    public Claims decryptToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }

    public String getUsername(String token) {
        Claims claims = this.decryptToken(token);
        String username = claims.getSubject();

        return username;
    }
}

