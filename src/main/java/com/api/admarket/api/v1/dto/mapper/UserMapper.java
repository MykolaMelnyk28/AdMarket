package com.api.admarket.api.v1.dto.mapper;

import com.api.admarket.api.v1.dto.user.UserDTO;
import com.api.admarket.api.v1.entity.user.UserEntity;

public interface UserMapper {
    UserDTO toDto(UserEntity entity);
    UserEntity toEntity(UserDTO dto);
}
