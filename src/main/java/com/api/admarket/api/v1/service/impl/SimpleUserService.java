package com.api.admarket.api.v1.service.impl;

import com.api.admarket.api.v1.entity.image.Image;
import com.api.admarket.api.v1.entity.user.AccountStatus;
import com.api.admarket.api.v1.entity.user.Role;
import com.api.admarket.api.v1.entity.user.UserEntity;
import com.api.admarket.api.v1.entity.user.UserInfo;
import com.api.admarket.api.v1.exeption.ResourceNotFoundException;
import com.api.admarket.api.v1.repository.UserRepository;
import com.api.admarket.api.v1.service.ImageService;
import com.api.admarket.api.v1.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

//@Service
public class SimpleUserService implements UserService {

    private final UserRepository userRepository;
    private final ImageService imageService;

    public SimpleUserService(UserRepository userRepository, ImageService imageService) {
        this.userRepository = userRepository;
        this.imageService = imageService;
    }

    @Override
    public UserEntity createUser(UserEntity user) {
        return create(user, false);
    }

    @Override
    public UserEntity createAdmin(UserEntity user) {
        return create(user, true);
    }

    private UserEntity create(UserEntity user, boolean isAdmin) {
        if (getByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("User already exists.");
        }
        user.setRoles(Set.of((isAdmin)
                ? Role.ROLE_ADMIN
                : Role.ROLE_USER));
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
        return found;
    }

    private void copy(UserEntity source, UserEntity destination) {
        destination.setUsername(source.getUsername());
        destination.setEmail(source.getEmail());
        destination.setPhoneNumber(source.getPhoneNumber());
        destination.setPasswordHash(source.getPasswordHash());
        destination.setRoles(source.getRoles());
        destination.setAccountStatus(source.getAccountStatus());

        if (source.getUserInfo() != null) {
            UserInfo sourceInfo = source.getUserInfo();
            UserInfo destinationInfo = destination.getUserInfo();
            destinationInfo.setFirstName(sourceInfo.getFirstName());
            destinationInfo.setLastName(sourceInfo.getLastName());
            destinationInfo.setSurName(sourceInfo.getSurName());
            destinationInfo.setCity(sourceInfo.getCity());
            destinationInfo.setStreet(sourceInfo.getStreet());
            destinationInfo.setPostalCode(sourceInfo.getPostalCode());
            destinationInfo.setState(sourceInfo.getState());
            destinationInfo.setAdditionalInfo(sourceInfo.getAdditionalInfo());
        }

        destination.setImageUrls(source.getImageUrls());
        destination.setAds(source.getAds());
        destination.setSavedAds(source.getSavedAds());
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public String addImage(Long userId, Image image) {
        String url = imageService.upload(image);
        userRepository.addImage(userId, url);
        return url;
    }

    @Override
    public void deleteImage(Long userId, String url) {
        imageService.unload(url);
        userRepository.deleteImage(userId, url);
    }

    private UserEntity getByIdOrThrow(Long id)
            throws ResourceNotFoundException {
        return getById(id).orElseThrow(() ->
                new ResourceNotFoundException("User not found.")
        );
    }

}
