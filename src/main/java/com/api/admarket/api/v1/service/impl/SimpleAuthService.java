package com.api.admarket.api.v1.service.impl;

import com.api.admarket.api.v1.dto.auth.JwtRequest;
import com.api.admarket.api.v1.dto.auth.JwtResponse;
import com.api.admarket.api.v1.dto.user.UserDTO;
import com.api.admarket.api.v1.entity.user.UserEntity;
import com.api.admarket.api.v1.service.AuthService;
import com.api.admarket.api.v1.service.UserService;
import com.api.admarket.config.security.JwtEntity;
import com.api.admarket.config.security.JwtEntityFactory;
import com.api.admarket.config.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimpleAuthService implements AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;

    @Override
    public JwtResponse register(UserEntity user) {
        UserEntity created = userService.create(user);

        JwtResponse response = createResponse(created);

        return response;
    }

    @Override
    public JwtResponse login(JwtRequest jwtRequest) {
        UserEntity user = userService.getByUsername(jwtRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        jwtRequest.getUsername(),
                        jwtRequest.getPassword()
                )
        );

        JwtResponse response = createResponse(user);

        return response;
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
        
        return null;
    }

}
