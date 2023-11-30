package com.api.admarket.api.v1.service;

import com.api.admarket.api.v1.entity.image.Image;
import com.api.admarket.api.v1.entity.user.AccountStatus;
import com.api.admarket.api.v1.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserEntity createUser(UserEntity user);
    UserEntity createAdmin(UserEntity user);
    Optional<UserEntity> getById(Long userId);
    Optional<UserEntity> getByUsername(String username);
    Optional<UserEntity> getByEmail(String email);
    Optional<UserEntity> getByPhoneNumber(String phoneNumber);
    Optional<UserEntity> getByUsernameOrEmail(String usernameOrEmail);
    Page<UserEntity> getAll(Pageable pageable);
    List<UserEntity> getAllByAccountStatus(AccountStatus accountStatus);
    UserEntity updateById(Long userId, UserEntity user);
    void deleteById(Long userId);
    void uploadImage(Long userId, Image image);

}