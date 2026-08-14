package com.fcpv.abnormal.service.impl;

import com.fcpv.abnormal.enums.TokenType;
import com.fcpv.abnormal.exception.InvalidDataException;
import com.fcpv.abnormal.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

import static com.fcpv.abnormal.enums.TokenType.ACCESS_TOKEN;
import static com.fcpv.abnormal.enums.TokenType.REFRESH_TOKEN;

@Service
@Slf4j(topic = "JWT-SERVICE")
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.expiryMinutes}")
    private long expiryMinutes;

    @Value("${jwt.expiryDay}")
    private long expiryDay;

    @Value("${jwt.accessKey}")
    private String accessKey;

    @Value("${jwt.refreshKey}")
    private String refreshKey;

    @Override
    public String generateAccessToken(String userCode, List<String> authorities) {
        log.info("Generate access token for userCode {} with authorities {} ", userCode, authorities);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", authorities);

        return generateToken(claims, userCode);
    }

    @Override
    public String generateRefreshToken(String userCode, List<String> authorities) {
        log.info("Generate refresh token for userCode {} with authorities {} ", userCode, authorities);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", authorities);

        return generateRefreshToken(claims, userCode);
    }

    @Override
    public String extractUserCode(String token, TokenType type) {
        log.info("Extract userCode for user {} with authorities {} ", token, type);
        return extractClaims(type, token, Claims::getSubject);
    }

    private <T> T extractClaims(TokenType type, String token, Function<Claims, T> claimsExtractor) {
        final Claims claims = extraAllClaim(token, type);
        return claimsExtractor.apply(claims);
    }

    private Claims extraAllClaim(String token, TokenType type) {
        try {
            return Jwts.parser()
                    .setSigningKey(getKey(type))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException | ExpiredJwtException e) {
            throw new AccessDeniedException("Access denied!, error: " + e.getMessage());
        }
    }

    private String generateToken(Map<String, Object> claims, String userCode) {
//        log.info("Generate access token for user {} with name {} ", userCode, claims);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userCode)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * expiryMinutes))
                .signWith(getKey(ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateRefreshToken(Map<String, Object> claims, String userCode) {
//        log.info("Generate refresh token for user {} with name {} ", userCode, claims);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userCode)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * expiryDay))
                .signWith(getKey(REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey(TokenType type) {
        switch (type) {
            case ACCESS_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessKey));
            }
            case REFRESH_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshKey));
            }
            default -> throw new InvalidDataException("Invalid token type");
        }
    }
}
