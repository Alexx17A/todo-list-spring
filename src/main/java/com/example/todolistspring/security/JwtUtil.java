//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.todolistspring.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private int expiration;

    public JwtUtil() {
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(this.secret.getBytes());
    }

    public String generateToken(String username) {
        return Jwts.builder().subject(username).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + (long)this.expiration)).signWith(this.getSigningKey()).compact();
    }

    public String getUsernameFromToken(String token) {
        return ((Claims)Jwts.parser().verifyWith(this.getSigningKey()).build().parseSignedClaims(token).getPayload()).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(this.getSigningKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception var3) {
            return false;
        }
    }
}
