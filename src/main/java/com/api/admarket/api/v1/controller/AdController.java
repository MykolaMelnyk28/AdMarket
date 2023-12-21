package com.api.admarket.api.v1.controller;

import com.api.admarket.api.v1.dto.ad.AdDTO;
import com.api.admarket.api.v1.dto.ad.FilterRequest;
import com.api.admarket.api.v1.dto.mapper.AdMapper;
import com.api.admarket.api.v1.dto.mapper.FiltersMapper;
import com.api.admarket.api.v1.dto.validation.OnUpdate;
import com.api.admarket.api.v1.entity.ad.AdEntity;
import com.api.admarket.api.v1.entity.ad.FilterProperties;
import com.api.admarket.api.v1.entity.image.Image;
import com.api.admarket.api.v1.service.AdService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ads")
@AllArgsConstructor
@Validated
@Tag(name = "Ad Controller", description = "Ad API")
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

    @PostMapping("/{adId}/images")
    @PreAuthorize("hasRole('ADMIN') or @simpleUserService.isAdSeller(#authentication.name, #adId)")
    public ResponseEntity<String> addImage(
            @PathVariable Long adId,
            @RequestPart("file") MultipartFile file
    ) {
        Image image = new Image();
        image.setFile(file);
        adService.addImage(adId, image);
        return ResponseEntity.ok(adService.getImages(adId).get(0));
    }

    @GetMapping("/{adId}/images")
    public ResponseEntity<List<String>> getAllImages(
            @PathVariable Long adId
    ) {
        return ResponseEntity.ok(adService.getImages(adId));
    }

    @DeleteMapping("/{adId}/images/{filename}")
    @PreAuthorize("hasRole('ADMIN') or @simpleUserService.isAdSeller(#authentication.name, #adId)")
    public HttpStatus deleteImage(
            @PathVariable Long adId,
            @PathVariable String filename
    ) {
        adService.deleteImage(adId, filename);
        return HttpStatus.OK;
    }
}
