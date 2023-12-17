package com.api.admarket.api.v1.service.impl;

import com.api.admarket.api.v1.dto.auth.JwtRequest;
import com.api.admarket.api.v1.dto.auth.JwtResponse;
import com.api.admarket.api.v1.entity.user.AccountStatus;
import com.api.admarket.api.v1.entity.user.UserEntity;
import com.api.admarket.api.v1.exeption.UserAccountStateException;
import com.api.admarket.api.v1.service.AuthService;
import com.api.admarket.api.v1.service.UserService;
import com.api.admarket.config.security.JwtEntity;
import com.api.admarket.config.security.JwtEntityFactory;
import com.api.admarket.config.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimpleAuthService implements AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    @Override
    public JwtResponse authenticate(JwtRequest jwtRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            throw new IllegalStateException("Authentication is already done.");
        }

        UserEntity user = userService.getByUsername(jwtRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if(user.getAccountStatus() == AccountStatus.BLOCKED) {
            throw new UserAccountStateException("Account is blocked.");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                jwtRequest.getUsername(),
                jwtRequest.getPassword()
        );
        System.out.println(authentication);
        authManager.authenticate(authentication);

        return createResponse(user);
    }

    private JwtResponse createResponse(UserEntity user) {
        JwtEntity jwtEntity = JwtEntityFactory.create(user);

        String accessToken = jwtService.generateAccessToken(jwtEntity);
        String refreshToken = jwtService.generateRefreshToken(jwtEntity);

        JwtResponse response = new JwtResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);

        return response;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return jwtService.refreshUserTokens(refreshToken);
    }

}
