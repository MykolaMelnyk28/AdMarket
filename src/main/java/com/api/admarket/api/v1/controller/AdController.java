package com.api.admarket.api.v1.controller;

import com.api.admarket.api.v1.dto.ad.AdDTO;
import com.api.admarket.api.v1.dto.ad.FilterRequest;
import com.api.admarket.api.v1.dto.mapper.AdMapper;
import com.api.admarket.api.v1.dto.mapper.FiltersMapper;
import com.api.admarket.api.v1.dto.validation.OnCreate;
import com.api.admarket.api.v1.dto.validation.OnUpdate;
import com.api.admarket.api.v1.entity.ad.AdEntity;
import com.api.admarket.api.v1.entity.ad.FilterProperties;
import com.api.admarket.api.v1.entity.user.UserEntity;
import com.api.admarket.api.v1.service.AdService;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/ads")
@AllArgsConstructor
@Validated
public class AdController {

    private final AdService adService;
    private final AdMapper adMapper;
    private final FiltersMapper filtersMapper;

    @GetMapping
    public ResponseEntity<Page<AdDTO>> getAll(
            @RequestParam(required = false, defaultValue = "all") String category,
            @ModelAttribute FilterRequest request
    ) {
        Page<AdEntity> page = getPageAds(category, request);
        return ResponseEntity.ok(page.map(adMapper::toDto));
    }

    private Page<AdEntity> getPageAds(String categoryName, FilterRequest filters) {
        FilterProperties filterPsEntity = filtersMapper.toEntity(filters);
        filterPsEntity.setCategory(categoryName);
        return adService.getAllByFilter(filterPsEntity);
    }

    @GetMapping("/{adId}")
    public ResponseEntity<AdDTO> getById(
            @PathVariable Long adId
    ) {
        Optional<AdEntity> found = adService.getById(adId);
        Optional<AdDTO> response = found.map(adMapper::toDto);
        adService.incrementViews(adId);
        return ResponseEntity.of(response);
    }

    @PutMapping("/{adId}")
    @PreAuthorize("@simpleUserService.isAdSeller(#authentication.name, #adId)")
    public ResponseEntity<AdDTO> updateById(
            @PathVariable Long adId,
            @RequestBody @Validated(OnUpdate.class) AdDTO request
    ) {
        AdEntity requestEntity = adMapper.toEntity(request);
        AdEntity updated = adService.updateById(adId, requestEntity);
        AdDTO response = adMapper.toDto(updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{adId}")
    @PreAuthorize("@simpleUserService.isAdSeller(#authentication.name, #adId)")
    public HttpStatus deleteById(
            @PathVariable Long adId
    ) {
        adService.deleteById(adId);
        return HttpStatus.OK;
    }
}
