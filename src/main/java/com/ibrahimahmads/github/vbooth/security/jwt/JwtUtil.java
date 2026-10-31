package com.ibrahimahmads.github.vbooth.security.jwt;

import com.ibrahimahmads.github.vbooth.security.model.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.access-token-cookie-name}")
    private String cokieName;
    @Value("${app.cookie.secure}")
    private Boolean cokieSecure;
    @Value("${app.cookie.same-site}")
    private String cokieSameSite;

    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Authentication authentication) {
        UserContext userContext = (UserContext) authentication.getPrincipal();
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return generateToken(authentication.getName(), userContext.getAccountId(), roles);
    }

    public String generateToken(String username, UUID accountId, List<String> roles) {
        return Jwts.builder()
                .subject(username)
                .claim("accountId", accountId)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public ResponseCookie generateJwtCookie(String token) {
        return ResponseCookie.from(cokieName, token)
                .httpOnly(true)
                .secure(cokieSecure)
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite(cokieSameSite)
                .build();
    }
}