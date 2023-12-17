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
import org.springframework.security.access.prepost.PreAuthorize;
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

    private final UserService userService;
    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/register/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> registerAdmin(
            @RequestBody @Validated(OnCreate.class) UserDTO request
    ) {
        UserEntity entity = userMapper.toEntity(request);
        entity.setRoles(Set.of(Role.ROLE_ADMIN));
        entity = userService.create(entity);
        return new ResponseEntity<>(userMapper.toDto(entity), HttpStatus.CREATED);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(
            @RequestBody @Validated(OnCreate.class) UserDTO request
    ) {
        UserEntity entity = userMapper.toEntity(request);
        entity.setRoles(Set.of(Role.ROLE_USER));
        entity = userService.create(entity);
        return new ResponseEntity<>(userMapper.toDto(entity), HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @RequestBody JwtRequest request
    ) {
        JwtResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }

}
