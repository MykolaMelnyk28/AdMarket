package com.api.admarket.api.v1.controller;

import com.api.admarket.api.v1.dto.ad.AdDTO;
import com.api.admarket.api.v1.dto.mapper.AdMapper;
import com.api.admarket.api.v1.dto.mapper.UserMapper;
import com.api.admarket.api.v1.dto.user.UserDTO;
import com.api.admarket.api.v1.dto.validation.OnCreate;
import com.api.admarket.api.v1.dto.validation.OnUpdate;
import com.api.admarket.api.v1.entity.ad.AdEntity;
import com.api.admarket.api.v1.entity.ad.SavedAd;
import com.api.admarket.api.v1.entity.user.UserEntity;
import com.api.admarket.api.v1.service.AdService;
import com.api.admarket.api.v1.service.UserService;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final AdService adService;
    private final UserMapper userMapper;
    private final AdMapper adMapper;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(
            @PathVariable Long id
    ) {
        Optional<UserEntity> found = userService.getById(id);
        return ResponseEntity.of(found.map(userMapper::toDto));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDTO>> getAll() {
        Page<UserEntity> pageEntity = userService.getAll(PageRequest.ofSize(10));
        Page<UserDTO> page = pageEntity.map(userMapper::toDto);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @simpleUserService.isUserEqual(authentication, #id)")
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
    @PreAuthorize("hasRole('ADMIN') or @simpleUserService.isUserEqual(authentication, #id)")
    public HttpStatus deleteById(
            @PathVariable Long id
    ) {
        userService.deleteById(id);
        return HttpStatus.OK;
    }

    @GetMapping("/{userId}/ads")
    public ResponseEntity<Page<AdDTO>> getAllAds(
            @PathVariable Long userId
    ) {
        Page<AdEntity> pageEntity = adService.getAllByUserId(userId, PageRequest.ofSize(10));
        Page<AdDTO> pageDto = pageEntity.map(adMapper::toDto);
        return ResponseEntity.ok(pageDto);
    }

    @PostMapping("/{userId}/ads")
    @PreAuthorize("@simpleUserService.isUserEqual(authentication, #userId)")
    public ResponseEntity<AdDTO> create(
            @PathVariable Long userId,
            @RequestBody @Validated(OnCreate.class) AdDTO dto
    ) {
        AdEntity entity = adMapper.toEntity(dto);
        AdEntity created = adService.create(entity, userId);
        AdDTO response = adMapper.toDto(created);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{userId}/saved/ads/{adId}")
    @PreAuthorize("@simpleUserService.isUserEqual(authentication, #userId)")
    public ResponseEntity<AdDTO> addSaveAdById(
            @PathVariable Long userId,
            @PathVariable Long adId
    ) {
        AdEntity entity = adService.appendSaveAd(userId, adId);
        AdDTO dto = adMapper.toDto(entity);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{userId}/saved/ads")
    @PreAuthorize("@simpleUserService.isUserEqual(authentication, #userId)")
    public ResponseEntity<Page<AdDTO>> getAllAdFromSavedList(
            @PathVariable Long userId
    ) {
        Page<AdEntity> ads = adService.getAllSavedAdsByUserId(userId, PageRequest.ofSize(10));
        return ResponseEntity.ok(ads.map(adMapper::toDto));
    }

    @DeleteMapping("/{userId}/saved/ads/{adId}")
    @PreAuthorize("@simpleUserService.isUserEqual(authentication, #userId)")
    public HttpStatus deleteAdFromSavedList(
            @PathVariable Long userId,
            @PathVariable Long adId
    ) {
        adService.deleteSavedAd(userId, adId);
        return HttpStatus.OK;
    }

}
