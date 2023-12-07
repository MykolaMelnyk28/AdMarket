package com.api.admarket.api.v1.controller;

import com.api.admarket.api.v1.dto.mapper.UserMapper;
import com.api.admarket.api.v1.dto.user.UserDTO;
import com.api.admarket.api.v1.dto.validation.OnCreate;
import com.api.admarket.api.v1.dto.validation.OnUpdate;
import com.api.admarket.api.v1.entity.user.UserEntity;
import com.api.admarket.api.v1.service.UserService;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/admin")
    public ResponseEntity<UserDTO> createAdmin(
            @RequestBody @Validated(OnCreate.class) UserDTO request
    ) {
        return ResponseEntity.ok(createResponse(request, true));
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(
            @RequestBody @Validated(OnCreate.class) UserDTO request
    ) {
        return ResponseEntity.ok(createResponse(request, false));
    }

    private UserDTO createResponse(UserDTO request, boolean isAdmin) {
        UserEntity user = userMapper.toEntity(request);
        UserEntity created = (isAdmin)
                ? userService.createAdmin(user)
                : userService.createUser(user);
        UserDTO response = userMapper.toDto(created);
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(
            @PathVariable Long id
    ) {
        Optional<UserEntity> found = userService.getById(id);
        return ResponseEntity.of(found.map(userMapper::toDto));
    }

    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAll() {
        Page<UserEntity> pageEntity = userService.getAll(PageRequest.ofSize(10));
        Page<UserDTO> page = pageEntity.map(userMapper::toDto);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateById(
            @PathVariable Long id,
            @RequestBody @Validated(OnUpdate.class) UserDTO request
    ) {
        UserEntity requestEntity = userMapper.toEntity(request);
        UserEntity updated = userService.updateById(id, requestEntity);
        UserDTO dto = userMapper.toDto(updated);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteById(
            @PathVariable Long id
    ) {
        userService.deleteById(id);
        return HttpStatus.OK;
    }
}
