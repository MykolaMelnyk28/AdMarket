package com.api.admarket.api.v1.controller;

import com.api.admarket.api.v1.dto.auth.JwtRequest;
import com.api.admarket.api.v1.dto.auth.JwtResponse;
import com.api.admarket.api.v1.dto.mapper.UserMapper;
import com.api.admarket.api.v1.dto.user.UserDTO;
import com.api.admarket.api.v1.dto.validation.OnCreate;
import com.api.admarket.api.v1.entity.user.Role;
import com.api.admarket.api.v1.entity.user.UserEntity;
import com.api.admarket.api.v1.service.AuthService;
import com.api.admarket.api.v1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/register/admin")
    public ResponseEntity<JwtResponse> registerAdmin(
            @RequestBody @Validated(OnCreate.class) UserDTO request
    ) {
        UserEntity entity = userMapper.toEntity(request);
        entity.setRoles(Set.of(Role.ROLE_ADMIN));
        JwtResponse response = authService.register(entity);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> registerUser(
            @RequestBody @Validated(OnCreate.class) UserDTO request
    ) {
        UserEntity entity = userMapper.toEntity(request);
        entity.setRoles(Set.of(Role.ROLE_USER));
        JwtResponse response = authService.register(entity);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @RequestBody JwtRequest request
    ) {
        JwtResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

}
