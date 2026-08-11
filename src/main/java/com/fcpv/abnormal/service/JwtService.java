package com.fcpv.abnormal.service;

import com.fcpv.abnormal.enums.TokenType;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public interface JwtService {

    String generateAccessToken(String userCode, List<String> authorities);

    String generateRefreshToken(String userCode, List<String> authorities);

    String extractUserCode(String token, TokenType tokenType);
}
