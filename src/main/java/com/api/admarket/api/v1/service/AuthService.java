package com.api.admarket.api.v1.service;

import com.api.admarket.api.v1.dto.auth.*;
import com.api.admarket.api.v1.entity.user.UserEntity;

public interface AuthService {

    JwtResponse authenticate(JwtRequest jwtRequest);
    JwtResponse refresh(String refreshToken);

}
