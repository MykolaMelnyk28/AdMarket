package com.api.admarket.api.v1.dto.mapper.impl;

import com.api.admarket.api.v1.dto.mapper.UserMapper;
import com.api.admarket.api.v1.dto.user.UserDTO;
import com.api.admarket.api.v1.entity.user.AccountStatus;
import com.api.admarket.api.v1.entity.user.Role;
import com.api.admarket.api.v1.entity.user.UserEntity;
import com.api.admarket.api.v1.entity.user.UserInfo;
import com.api.admarket.api.v1.service.ImageService;
import com.api.admarket.api.v1.service.UserService;
import com.api.admarket.api.v1.service.impl.MinioImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.imageio.stream.ImageInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SimpleUserMapper implements UserMapper {

    private final UserService userService;

    @Override
    public UserDTO toDto(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        UserDTO dto = new UserDTO();

        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setPassword(entity.getPasswordHash());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setDateRegistered(entity.getDateRegistered());
        dto.setDateUpdated(entity.getDateUpdated());
        dto.setAccountStatus(entity.getAccountStatus().name().toLowerCase());
        dto.setRole(getStringRole(entity.getRoles()));
        copyUserInfo(dto, entity.getUserInfo());

        dto.setImages(getImages(entity.getId()));

        return dto;
    }

    private String getStringRole(Set<Role> roles) {
        Role role = roles.iterator().next();
        return role.name().substring(5).toLowerCase();
    }

    private List<String> getImages(Long id) {
        return userService.getImages(id);
    }

    private void copyUserInfo(UserDTO destination, UserInfo source) {
        if (source != null) {
            destination.setFirstName(source.getFirstName());
            destination.setLastName(source.getLastName());
            destination.setSurName(source.getSurName());
            destination.setCountry(source.getCountry());
            destination.setCity(source.getCity());
            destination.setStreet(source.getStreet());
            destination.setPostalCode(source.getPostalCode());
            destination.setState(source.getState());
            destination.setAdditionalAddressInfo(source.getAdditionalInfo());
        }
    }

    @Override
    public UserEntity toEntity(UserDTO dto) {
        UserEntity entity = new UserEntity();
        entity.setId(dto.getId());
        entity.setUsername(dto.getUsername());
        entity.setPasswordHash(dto.getPassword());
        entity.setEmail(dto.getEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setUserInfo(getUserInfoOrNull(dto));
        entity.setDateRegistered(dto.getDateRegistered());
        entity.setDateUpdated(dto.getDateUpdated());
        entity.setRoles(getUserRole(dto.getRole()));
        if (dto.getAccountStatus() != null) {
            entity.setAccountStatus(AccountStatus.valueOf(dto.getAccountStatus().toUpperCase()));
        }
        entity.setImageUrls((dto.getImages() != null) ? dto.getImages() : new ArrayList<>());

        return entity;
    }

    private Set<Role> getUserRole(String role) {
        if (role == null) {
            return null;
        }
        String roleBuilt = "ROLE_" + role.toUpperCase();
        return Set.of(Role.valueOf(roleBuilt));
    }

    private UserInfo getUserInfoOrNull(UserDTO source) {
        if (!isExistsUserInfo(source)) {
            return null;
        }
        UserInfo info = new UserInfo();
        info.setFirstName(source.getFirstName());
        info.setLastName(source.getLastName());
        info.setSurName(source.getSurName());
        info.setCountry(source.getCountry());
        info.setCity(source.getCity());
        info.setStreet(source.getStreet());
        info.setPostalCode(source.getPostalCode());
        info.setState(source.getState());
        info.setAdditionalInfo(source.getAdditionalAddressInfo());
        return info;
    }

    private boolean isExistsUserInfo(UserDTO source) {
        return source.getFirstName() != null ||
                source.getLastName() != null ||
                source.getSurName() != null ||
                source.getCountry() != null ||
                source.getCity() != null ||
                source.getStreet() != null ||
                source.getPostalCode() != null ||
                source.getState() != null ||
                source.getAdditionalAddressInfo() != null;
    }
}
