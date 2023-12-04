package com.api.admarket.api.v1.service.impl;

import com.api.admarket.api.v1.dto.auth.JwtRequest;
import com.api.admarket.api.v1.dto.auth.JwtResponse;
import com.api.admarket.api.v1.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class SimpleAuthService implements AuthService {

    @Override
    public JwtResponse register(JwtRequest jwtRequest) {
        return null;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return null;
    }
}
