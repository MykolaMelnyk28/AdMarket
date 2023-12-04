package com.api.admarket.api.v1.service;

import com.api.admarket.api.v1.dto.auth.*;

public interface AuthService {

    JwtResponse register(JwtRequest jwtRequest);
    JwtResponse refresh(String refreshToken);

}
