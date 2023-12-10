package com.api.admarket.api.v1.service.impl;

import com.api.admarket.api.v1.entity.image.Image;
import com.api.admarket.api.v1.entity.user.AccountStatus;
import com.api.admarket.api.v1.entity.user.Role;
import com.api.admarket.api.v1.entity.user.UserEntity;
import com.api.admarket.api.v1.entity.user.UserInfo;
import com.api.admarket.api.v1.exeption.ResourceNotFoundException;
import com.api.admarket.api.v1.repository.UserRepository;
import com.api.admarket.api.v1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SimpleUserService implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    //private final ImageService imageService;


    @Override
    public UserEntity create(UserEntity user) {
        if (getByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("User already exists.");
        }
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        UserEntity saved = userRepository.save(user);
        return saved;
    }

    @Override
    public Optional<UserEntity> getById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserEntity> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<UserEntity> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public Optional<UserEntity> getByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Optional<UserEntity> getByUsernameOrEmail(String usernameOrEmail) {
        return getByUsername(usernameOrEmail).or(() -> getByEmail(usernameOrEmail));
    }

    @Override
    public Page<UserEntity> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<UserEntity> getAllByAccountStatus(AccountStatus accountStatus, Pageable pageable) {
        return userRepository.findAllByAccountStatus(accountStatus, pageable);
    }

    @Override
    public UserEntity updateById(Long id, UserEntity user) {
        UserEntity found = getByIdOrThrow(id);
        copy(user, found);
        userRepository.save(found);
        return found;
    }

    private void copy(UserEntity source, UserEntity destination) {
        Class<?> sourceClass = source.getClass();
        Field[] fields = sourceClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            try {
                Object value = field.get(source);

                if (value != null) {
                    if (value instanceof UserInfo) {
                        copyUserInfo((UserInfo)value, destination.getUserInfo());
                    } else {
                        field.set(destination, value);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void copyUserInfo(UserInfo source, UserInfo destination) {
        Class<?> sourceClass = source.getClass();
        Field[] fields = sourceClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            try {
                Object value = field.get(source);
                if (value != null) {
                    field.set(destination, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAdSeller(String username, Long adId) {
        UserEntity user = getByUsername(username).orElseThrow(() ->
                new ResourceNotFoundException("User not found"));
        return userRepository.isAdSeller(user.getId(), adId);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public String addImage(Long userId, Image image) {
//        String url = imageService.upload(image);
//        userRepository.addImage(userId, url);
//        return url;
        return "";
    }

    @Override
    public void deleteImage(Long userId, String url) {
//        imageService.unload(url);
//        userRepository.deleteImage(userId, url);
    }

    private UserEntity getByIdOrThrow(Long id)
            throws ResourceNotFoundException {
        return getById(id).orElseThrow(() ->
                new ResourceNotFoundException("User not found.")
        );
    }

}
