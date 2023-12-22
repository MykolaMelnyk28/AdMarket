package com.api.admarket.api.v1.service;

import com.api.admarket.api.v1.entity.image.Image;
import com.api.admarket.api.v1.entity.user.AccountStatus;
import com.api.admarket.api.v1.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserEntity create(UserEntity user);
    Optional<UserEntity> getById(Long userId);
    Optional<UserEntity> getByUsername(String username);
    Optional<UserEntity> getByEmail(String email);
    Optional<UserEntity> getByPhoneNumber(String phoneNumber);
    Optional<UserEntity> getByUsernameOrEmail(String usernameOrEmail);
    Page<UserEntity> getAll(Pageable pageable);
    Page<UserEntity> getAllByAccountStatus(AccountStatus accountStatus, Pageable pageable);
    UserEntity updateById(Long userId, UserEntity user);
    void deleteById(Long userId);

    boolean isAdSeller(String username, Long adId);

    boolean isUserEqual(Authentication authentication, Long userId);

    String addImage(Long userId, Image image);
    List<String> getImages(Long userId);
    void deleteImage(Long userId, String filename);

}
