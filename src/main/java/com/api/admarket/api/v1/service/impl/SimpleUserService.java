package com.api.admarket.api.v1.service.impl;

import com.api.admarket.api.v1.entity.image.Image;
import com.api.admarket.api.v1.entity.user.AccountStatus;
import com.api.admarket.api.v1.entity.user.UserEntity;
import com.api.admarket.api.v1.entity.user.UserInfo;
import com.api.admarket.api.v1.exeption.ResourceNotFoundException;
import com.api.admarket.api.v1.repository.UserRepository;
import com.api.admarket.api.v1.service.ImageService;
import com.api.admarket.api.v1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SimpleUserService implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;


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
    public boolean isUserEqual(Authentication authentication, Long userId) {
        if (authentication == null || authentication.getName() == null) {
            return false;
        }
        UserEntity user = getByIdOrThrow(userId);
        return user.getUsername().equals(authentication.getName());
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public String addImage(Long userId, Image image) {
        UserEntity user = getByIdOrThrow(userId);
        String filename = createFilename(userId, image.getFile().getOriginalFilename());
        user.getImageUrls().add(filename);
        userRepository.save(user);
        imageService.upload(filename, image);
        return imageService.getUrl(filename, 1);
    }

    @Override
    public List<String> getImages(Long userId) {
        String directory = "users/" + userId + "/images";
        return imageService.getUrls(directory, 1);
    }

    @Override
    public void deleteImage(Long userId, String filename) {
        String filenameFull = createFilename(userId, filename);
        imageService.delete(filenameFull);
        userRepository.deleteImage(userId, filenameFull);
    }

    private String createFilename(Long adId, String originalName) {
        StringBuilder builder = new StringBuilder().append("users/")
                .append(adId)
                .append("/images/")
                .append(originalName);
        return builder.toString();
    }

    private UserEntity getByIdOrThrow(Long id)
            throws ResourceNotFoundException {
        return getById(id).orElseThrow(() ->
                new ResourceNotFoundException("User not found.")
        );
    }

}
