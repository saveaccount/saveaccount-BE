package com.example.savemoney.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {

    private Key key;

    // JWT는 String key를 쓰지 않음, 객체 key 생성
    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {

        byte[] byteSecretKey = Decoders.BASE64.decode(secret);
        key = Keys.hmacShaKeyFor(byteSecretKey);
    }

    // 검증
    public String getUsername(String token) {

        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("username", String.class);
    }

    public String getRole(String token) {

        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("role", String.class);
    }

    public String getCategory(String token) {

        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("category", String.class);
    }

    // 소멸 됐는지 안 됐는지 boolean 값으로 확인, before()안에 현재 시간 넣음
    public Boolean isExpired(String token) {

        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }

    // 로그인이 완료 됐을 때 토큰 생성
    public String createJwt(String category, String username, String role, long expiredMs) {

        Claims claims = Jwts.claims();
        claims.put("username", username);
        claims.put("category", category);
//        claims.put("role", role);

        return Jwts.builder()
                .addClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
