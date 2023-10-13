package com.example.twitter.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component

import javax.servlet.http.HttpServletRequest
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.function.Function

@Component
class JwtUtil {
    private final Key secret
    @Value('${jwt.expiration}')
    private long expiration

    JwtUtil(@Value('${jwt.secret}') String secretString) {
        secret = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }

    String generateToken(String email) {
        Claims claims = Jwts.claims().setSubject(email)
        Date now = new Date()
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration))
                .signWith(secret)
                .compact()
    }

    String resolveToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7)
        }
        return null
    }

    boolean isTokenValid(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return claims.getBody().getExpiration().after(new Date())
        } catch (Exception e) {
            return false
        }
    }

    String getUserName(String token) {
        return getClaimsFromToken(token, Claims::getSubject)
    }

    private <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody()
        return claimsResolver.apply(claims)
    }
}
