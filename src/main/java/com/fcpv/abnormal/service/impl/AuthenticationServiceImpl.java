package com.fcpv.abnormal.service.impl;

import com.fcpv.abnormal.dto.request.SignInToken;
import com.fcpv.abnormal.dto.response.TokenResponse;
import com.fcpv.abnormal.enums.TokenType;
import com.fcpv.abnormal.exception.ForbiddenException;
import com.fcpv.abnormal.model.User;
import com.fcpv.abnormal.repository.UserRepository;
import com.fcpv.abnormal.service.AuthenticationService;
import com.fcpv.abnormal.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AUTHENTICATION-SERVICE")
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public TokenResponse getAccessToken(SignInToken request) {
        log.info("Get access token");

        List<String> authorities = new ArrayList<>();
        try {
            // Xác thực với userCode và password
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserCode(), request.getPassword()));

            log.info("isAuthenticate = {}", authenticate.isAuthenticated());
            log.info("Authorities: {}", authenticate.getAuthorities().toString());

            authorities.add(authenticate.getAuthorities().toString());

            // Nếu xác thực thành công, lưu thông tin vào SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authenticate);
        } catch (BadCredentialsException | DisabledException e) {
            log.error("errorMessage = {}", e.getMessage());
            throw new AccessDeniedException(e.getMessage());
        }

        var user = userRepository.findByUserCode(request.getUserCode());
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        String accessToken = jwtService.generateAccessToken(request.getUserCode(), authorities);
        String refreshToken = jwtService.generateRefreshToken(request.getUserCode(), authorities);
        String role = String.valueOf(user.getRole());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(role)
                .build();
    }

    @Override
    public TokenResponse getRefreshToken(String refreshToken) {

        try {
            log.info("Get refresh token");

            String userCode = jwtService.extractUserCode(refreshToken, TokenType.REFRESH_TOKEN);

            // check user is active or inactive
            User user = userRepository.findByUserCode(userCode);
            if(user == null) {
                throw new UsernameNotFoundException("User not found");
            }
            
            List<String> authorities = new ArrayList<>();
            user.getAuthorities().forEach(authority -> authorities.add(authority.getAuthority()));

            // generate new access token AND new refresh token (token rotation)
            String newAccessToken = jwtService.generateAccessToken(userCode, authorities);
            String newRefreshToken = jwtService.generateRefreshToken(userCode, authorities);

            return TokenResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .role(String.valueOf(user.getRole()))
                    .build();
        } catch (Exception e) {
            log.error("Access denied! errorMessage: {}", e.getMessage());
            throw new ForbiddenException(e.getMessage());
        }

    }
}
